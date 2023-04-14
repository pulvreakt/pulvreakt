package it.nicolasfarabegoli.pulverization.runtime.reconfiguration

import it.nicolasfarabegoli.pulverization.core.Initializable
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ComponentRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ComponentRef.OperationMode.Local
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ComponentRef.OperationMode.Remote
import it.nicolasfarabegoli.pulverization.runtime.componentsref.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef
import it.nicolasfarabegoli.pulverization.runtime.context.ExecutionContext
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ReconfigurationEvent
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ReconfigurationRules
import it.nicolasfarabegoli.pulverization.runtime.spawner.SpawnerManager
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class BehaviourRefsContainer<S : Any, C : Any, SS : Any, AS : Any>(
    val stateRef: StateRef<S>,
    val communicationRef: CommunicationRef<C>,
    val sensorsRef: SensorsRef<SS>,
    val actuatorsRef: ActuatorsRef<AS>,
)

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
    localStartComponents: Set<ComponentType>,
) : Initializable, KoinComponent {

    private val scope = CoroutineScope(Dispatchers.Default)
    private val executionContext: ExecutionContext by inject()
    private val rulesJobs = mutableSetOf<Job>()
    private var incomingReconfigurationJob: Job? = null
    private var localComponents = localStartComponents

    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("Koin module not initialized")

    override suspend fun initialize(): Unit = coroutineScope {
        incomingReconfigurationJob = scope.launch {
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

    private suspend fun spawnRulesObserver() {
        suspend fun <S : Any> ReconfigurationEvent<S>.checkRule(newConfig: Pair<ComponentType, Host>) {
            val (targetComponent, _) = newConfig
            events.filter { predicate(it) && targetComponent in localComponents }.collect {
                println("New reconfiguration!")
                changeComponentMode(newConfig)
                reconfigurator.reconfigure(newConfig)
            }
        }

        rules?.deviceReconfigurationRules
            ?.filter { it.reconfiguration.second != executionContext.host } // Take only legitimate rules
            ?.forEach {
                val job = scope.launch {
                    it.rule.checkRule(it.reconfiguration)
                }
                rulesJobs += job
            }
    }

    private suspend fun changeComponentMode(newConfiguration: Pair<ComponentType, Host>) {
        val (component, targetHost) = newConfiguration
        val moveToLocal = targetHost == executionContext.host // The component should be moved on this host?
        println("New config: $newConfiguration - $moveToLocal")
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
                    is Behaviour -> { /* Do nothing here */ }
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
