package it.unibo.pulvreakt.runtime.reconfiguration

import it.unibo.pulvreakt.core.Initializable
import it.unibo.pulvreakt.dsl.model.Actuators
import it.unibo.pulvreakt.dsl.model.Behaviour
import it.unibo.pulvreakt.dsl.model.Communication
import it.unibo.pulvreakt.dsl.model.ComponentType
import it.unibo.pulvreakt.dsl.model.Sensors
import it.unibo.pulvreakt.dsl.model.State
import it.unibo.pulvreakt.runtime.componentsref.ActuatorsRef
import it.unibo.pulvreakt.runtime.componentsref.BehaviourRef
import it.unibo.pulvreakt.runtime.componentsref.CommunicationRef
import it.unibo.pulvreakt.runtime.componentsref.ComponentRef
import it.unibo.pulvreakt.runtime.componentsref.ComponentRef.OperationMode.Local
import it.unibo.pulvreakt.runtime.componentsref.ComponentRef.OperationMode.Remote
import it.unibo.pulvreakt.runtime.componentsref.SensorsRef
import it.unibo.pulvreakt.runtime.componentsref.StateRef
import it.unibo.pulvreakt.runtime.context.ExecutionContext
import it.unibo.pulvreakt.runtime.dsl.model.FailOnReconfiguration
import it.unibo.pulvreakt.runtime.dsl.model.ReconfigurationEvent
import it.unibo.pulvreakt.runtime.dsl.model.ReconfigurationRules
import it.unibo.pulvreakt.runtime.dsl.model.ReconfigurationSuccess
import it.unibo.pulvreakt.runtime.dsl.model.SkipCheck
import it.unibo.pulvreakt.runtime.spawner.SpawnerManager
import it.unibo.pulvreakt.utils.PulverizationKoinModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * References from the behavior to [stateRef], [communicationRef], [sensorsRef], [actuatorsRef].
 */
data class BehaviourRefsContainer<S : Any, C : Any, SS : Any, AS : Any>(
    val stateRef: StateRef<S>,
    val communicationRef: CommunicationRef<C>,
    val sensorsRef: SensorsRef<SS>,
    val actuatorsRef: ActuatorsRef<AS>,
)

/**
 * References to the behaviour.
 * [behaviourRefs]
 * [stateToBehaviourRef]
 * [communicationToBehaviourRef]
 * [sensorsToBehaviourRef]
 * [actuatorsToBehaviourRef]
 */
data class ComponentsRefsContainer<S : Any, C : Any, SS : Any, AS : Any>(
    val behaviourRefs: BehaviourRefsContainer<S, C, SS, AS>,
    val stateToBehaviourRef: BehaviourRef<S>,
    val communicationToBehaviourRef: BehaviourRef<C>,
    val sensorsToBehaviourRef: BehaviourRef<SS>,
    val actuatorsToBehaviourRef: BehaviourRef<AS>,
)

/**
 * Class used for the reconfiguration of the deployment unit.
 * [reconfigurator] the component which propagate the new deployment.
 * [rules] the set of rules that should be checked.
 * [componentsRef] component references used to switch mode based on the rules.
 */
class UnitReconfigurator<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    private val reconfigurator: Reconfigurator,
    private val rules: ReconfigurationRules?,
    private val componentsRef: ComponentsRefsContainer<S, C, SS, AS>,
    private val spawner: SpawnerManager<S, C, SS, AS, O>,
    private val localStartComponents: Set<ComponentType>,
) : Initializable, KoinComponent {

    private val scope = CoroutineScope(Dispatchers.Default)
    private val executionContext: ExecutionContext by inject()
    private val rulesJobs = mutableSetOf<Job>()
    private var incomingReconfigurationJob: Job? = null
    private var localComponents = localStartComponents
    private val logger = KotlinLogging.logger("UnitReconfigurator")

    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("Koin module not initialized")

    override suspend fun initialize(): Unit = coroutineScope {
        logger.info { "Setup unit reconfigurator with local components: $localComponents" }
        localComponents = localStartComponents
        reconfigurator.initialize()
        incomingReconfigurationJob = scope.launch {
            logger.debug { "Spawned listener for incoming reconfiguration events" }
            reconfigurator.receiveReconfiguration().collect {
                changeComponentMode(it)
            }
        }
        spawnRulesObserver()
    }

    override suspend fun finalize() {
        rulesJobs.forEach { it.cancel() }
        incomingReconfigurationJob?.cancel()
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun spawnRulesObserver() {
        logger.info { "Spawning rules listener" }
        suspend fun <S : Any> ReconfigurationEvent<S>.checkRule(newConfig: NewConfiguration) {
            val (targetComponent, _) = newConfig
            events.collect {
                if (targetComponent in localComponents && predicate(it)) {
                    try {
                        logger.info { "New reconfiguration triggered" }
                        logger.debug { "Reconfiguration details: $newConfig" }
                        changeComponentMode(newConfig)
                        reconfigurator.reconfigure(newConfig)
                        onReconfigurationEvent(ReconfigurationSuccess(it))
                    } catch (ex: Exception) { onReconfigurationEvent(FailOnReconfiguration(it, ex)) }
                } else { onReconfigurationEvent(SkipCheck(it)) }
            }
            events.filter { predicate(it) && targetComponent in localComponents }.collect {
                logger.info { "New reconfiguration triggered" }
                logger.debug { "Reconfiguration details: $newConfig" }
                changeComponentMode(newConfig)
                reconfigurator.reconfigure(newConfig)
            }
        }
        logger.debug { "Spawning ${rules?.deviceReconfigurationRules?.size ?: 0} listeners" }
        rules?.deviceReconfigurationRules
            ?.filter { it.reconfiguration.second != executionContext.host.hostname } // Take only legitimate rules
            ?.forEach {
                val job = scope.launch {
                    it.rule.checkRule(it.reconfiguration)
                }
                rulesJobs += job
            }
    }

    private suspend fun changeComponentMode(newConfiguration: NewConfiguration) {
        val (component, targetHost) = newConfiguration
        val moveToLocal = targetHost == executionContext.host.hostname // The component should be moved on this host?
        logger.debug { "New reconfiguration: $newConfiguration" }
        logger.debug { "The components should move to local: $moveToLocal" }
        when (component) {
            is Behaviour -> component.manageReconfiguration(moveToLocal)
            is State -> component.manageReconfiguration(
                moveToLocal,
                componentsRef.stateToBehaviourRef,
                componentsRef.behaviourRefs.stateRef,
            )

            is Communication -> component.manageReconfiguration(
                moveToLocal,
                componentsRef.communicationToBehaviourRef,
                componentsRef.behaviourRefs.communicationRef,
            )

            is Sensors -> component.manageReconfiguration(
                moveToLocal,
                componentsRef.sensorsToBehaviourRef,
                componentsRef.behaviourRefs.sensorsRef,
            )

            is Actuators -> component.manageReconfiguration(
                moveToLocal,
                componentsRef.actuatorsToBehaviourRef,
                componentsRef.behaviourRefs.actuatorsRef,
            )
        }
    }

    private suspend fun ComponentType.manageReconfiguration(
        moveToLocal: Boolean,
        component: ComponentRef<*>,
        behaviour: ComponentRef<*>,
    ) {
        if (this !in localComponents && moveToLocal) { // Activation of local instance of the component
            spawner.spawn(this)
            component.operationMode = Local
            behaviour.operationMode = Local
            localComponents = localComponents + this
        } else if (this in localComponents && !moveToLocal) { // The component should move to another deployment unit
            spawner.kill(this)
            component.operationMode = Remote
            behaviour.operationMode = Remote
            localComponents = localComponents - this
        }
        // On the other case nothing should be done.
    }

    private suspend fun Behaviour.manageReconfiguration(moveToLocal: Boolean) {
        if (this !in localComponents && moveToLocal) { // Activation of local instance of the behaviour
            localComponents = localComponents + this
            spawner.spawn(Behaviour)
            localComponents.forEach {
                when (it) {
                    is Behaviour -> { /* Do nothing here */
                    }

                    is State -> {
                        componentsRef.stateToBehaviourRef.operationMode = Local
                        componentsRef.behaviourRefs.stateRef.operationMode = Local
                    }

                    is Communication -> {
                        componentsRef.communicationToBehaviourRef.operationMode = Local
                        componentsRef.behaviourRefs.communicationRef.operationMode = Local
                    }

                    is Sensors -> {
                        componentsRef.sensorsToBehaviourRef.operationMode = Local
                        componentsRef.behaviourRefs.sensorsRef.operationMode = Local
                    }

                    is Actuators -> {
                        componentsRef.actuatorsToBehaviourRef.operationMode = Local
                        componentsRef.behaviourRefs.actuatorsRef.operationMode = Local
                    }
                }
            }
        } else if (this in localComponents && !moveToLocal) {
            spawner.kill(Behaviour)
            componentsRef.stateToBehaviourRef.operationMode = Remote
            componentsRef.communicationToBehaviourRef.operationMode = Remote
            componentsRef.sensorsToBehaviourRef.operationMode = Remote
            componentsRef.actuatorsToBehaviourRef.operationMode = Remote
            localComponents = localComponents - this
        }
    }
}
