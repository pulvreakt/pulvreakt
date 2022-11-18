package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.runtime.ActuatorRef
import it.nicolasfarabegoli.pulverization.runtime.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.StateRef
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class RuntimeDSL(
    private val pureComponents: PureComponentsInstances,
    private val componentsReferences: ComponentsReferences,
) {

    private val jobsRef: MutableSet<Job> = mutableSetOf()

    fun withPlatform(): Nothing = TODO()

    @Suppress("UNCHECKED_CAST")
    suspend fun <C : SensorsContainer> withSensorsLogic(init: suspend (C, BehaviourRef) -> Unit) =
        coroutineScope {
            pureComponents.sensors?.let {
                launch { init(it as C, componentsReferences.behaviourRef) }
            }
        }

    @Suppress("UNCHECKED_CAST")
    suspend fun <C : ActuatorsContainer> withActuatorsLogic(init: suspend (C, BehaviourRef) -> Unit) = coroutineScope {
        pureComponents.actuators?.let {
            val job = launch { init(it as C, componentsReferences.behaviourRef) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <SR, C, W, A, O, B : Behaviour<SR, C, W, A, O>> withBehaviourLogic(
        init: suspend (B, SensorsRef?, ActuatorRef?, StateRef?, CommunicationRef?) -> Unit,
    ) where SR : StateRepresentation, C : CommunicationPayload = coroutineScope {
        pureComponents.behaviour?.let {
            val job = launch {
                init(
                    it as B,
                    componentsReferences.sensorsRef,
                    componentsReferences.actuatorRef,
                    componentsReferences.stateRef,
                    componentsReferences.communicationRef,
                )
            }
            jobsRef += job
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <S : StateRepresentation, C : State<S>> withStateLogic(init: suspend (C, BehaviourRef) -> Unit) =
        coroutineScope {
            pureComponents.state?.let {
                val job = launch { init(it as C, componentsReferences.behaviourRef) }
                jobsRef += job
            }
        }

    @Suppress("UNCHECKED_CAST")
    suspend fun <P, C : Communication<P>> withCommunicationLogic(
        init: suspend (C, BehaviourRef) -> Unit,
    ) = coroutineScope {
        pureComponents.communication?.let {
            val job = launch { init(it as C, componentsReferences.behaviourRef) }
            jobsRef += job
        }
    }

    fun build(): Set<Job> = TODO()
}

data class PureComponentsInstances(
    val state: State<*>?,
    val behaviour: Behaviour<*, *, *, *, *>?,
    val communication: Communication<*>?,
    val sensors: SensorsContainer?,
    val actuators: ActuatorsContainer?,
)

data class ComponentsReferences(
    val stateRef: StateRef,
    val behaviourRef: BehaviourRef,
    val communicationRef: CommunicationRef,
    val sensorsRef: SensorsRef,
    val actuatorRef: ActuatorRef,
)
