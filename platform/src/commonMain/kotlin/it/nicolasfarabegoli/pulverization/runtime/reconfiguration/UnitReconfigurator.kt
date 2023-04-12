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
import it.nicolasfarabegoli.pulverization.runtime.utils.Spawner
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal data class BehaviourRefsContainer<S : Any, C : Any, SS : Any, AS : Any>(
    val stateRef: StateRef<S>,
    val communicationRef: CommunicationRef<C>,
    val sensorsRef: SensorsRef<SS>,
    val actuatorsRef: ActuatorsRef<AS>,
)

internal data class ComponentsRefsContainer<S : Any, C : Any, SS : Any, AS : Any>(
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
internal class UnitReconfigurator<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    private val reconfigurator: Reconfigurator,
    private val rules: ReconfigurationRules?,
    private val componentsRef: ComponentsRefsContainer<S, C, SS, AS>,
    private val spawner: Spawner<S, C, SS, AS, O>,
    localStartComponents: Set<ComponentType>,
) : Initializable, KoinComponent {

    private val executionContext: ExecutionContext by inject()
    private val rulesJobs = mutableSetOf<Job>()
    private var incomingReconfigurationJob: Job? = null
    private var localComponents = localStartComponents

    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("Koin module not initialized")

    override suspend fun initialize(): Unit = coroutineScope {
        incomingReconfigurationJob = launch {
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

    private suspend fun spawnRulesObserver() = coroutineScope {
        suspend fun <S : Any> ReconfigurationEvent<S>.checkRule(newConfig: Pair<ComponentType, Host>) {
            val (targetComponent, _) = newConfig
            events.filter { predicate(it) && targetComponent in localComponents }.collect {
                changeComponentMode(newConfig)
                reconfigurator.reconfigure(newConfig)
            }
        }

        rules?.deviceReconfigurationRules
            ?.filter { it.reconfiguration.second != executionContext.host } // Take only legitimate rules
            ?.forEach {
                val job = launch {
                    it.rule.checkRule(it.reconfiguration)
                }
                rulesJobs += job
            }
    }

    private fun changeComponentMode(newConfiguration: Pair<ComponentType, Host>) {
        val (component, targetHost) = newConfiguration
        val moveToLocal = targetHost == executionContext.host // The component should be moved on this host?
        when (component) {
            is Actuators -> component.manageReconfiguration(
                moveToLocal,
                componentsRef.actuatorsToBehaviourRef,
                componentsRef.behaviourRefs.actuatorsRef,
            )
            is Behaviour -> component.manageReconfiguration(moveToLocal)
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
            is State -> component.manageReconfiguration(
                moveToLocal,
                componentsRef.stateToBehaviourRef,
                componentsRef.behaviourRefs.stateRef,
            )
        }
    }

    private fun ComponentType.manageReconfiguration(
        moveToLocal: Boolean,
        component: ComponentRef<*>,
        behaviour: ComponentRef<*>,
    ) {
        if (this !in localComponents && moveToLocal) { // Activation of local instance of the component
            // TODO(Capire se far partire l'istanza locale del componente oppure non e' necessario)
            component.setOperationMode(Local)
            behaviour.setOperationMode(Local)
            localComponents = localComponents + this
        } else if (this in localComponents && !moveToLocal) { // The component should move to another deployment unit
            // TODO(Capire se killare l'istanza locale del componente oppure non e' necessario)
            component.setOperationMode(Remote)
            behaviour.setOperationMode(Remote)
            localComponents = localComponents - this
        }
        // On the other case nothing should be done.
    }

    private fun Behaviour.manageReconfiguration(
        moveToLocal: Boolean,
    ) {
        if (this !in localComponents && moveToLocal) { // Activation of local instance of the behaviour
            localComponents = localComponents + this
            TODO()
        } else if (this in localComponents && !moveToLocal) {
            // TODO(capire se killare il behaviour)
            componentsRef.stateToBehaviourRef.setOperationMode(Remote)
            componentsRef.communicationToBehaviourRef.setOperationMode(Remote)
            componentsRef.sensorsToBehaviourRef.setOperationMode(Remote)
            componentsRef.actuatorsToBehaviourRef.setOperationMode(Remote)
            localComponents = localComponents - this
        }
    }
}
