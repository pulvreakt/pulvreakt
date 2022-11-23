package it.nicolasfarabegoli.pulverization.runtime

import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

fun <S, C, SS, AS, R> pulverizationPlatform(init: PulverizationPlatformScope<S, C, SS, AS, R>.() -> Unit): Nothing
    where S : StateRepresentation, C : CommunicationPayload = TODO()

class PulverizationPlatformScope<S, C, SS, AS, R> where S : StateRepresentation, C : CommunicationPayload {
    var stateLogic: (suspend (State<S>, BehaviourRef) -> Unit)? = null
    var actuatorsLogic: (suspend (ActuatorsContainer, BehaviourRef) -> Unit)? = null
    var sensorsLogic: (suspend (SensorsContainer, BehaviourRef) -> Unit)? = null
    var communicationLogic: (suspend (Communication<C>, BehaviourRef) -> Unit)? = null
    var behaviourLogic:
        (suspend (Behaviour<S, C, SS, AS, R>, StateRef, SensorsRef, ActuatorRef, CommunicationRef) -> Unit)? = null

    suspend fun start(): Set<Job> = coroutineScope {
        val stateJob = stateLogic?.let { f -> launch { f(TODO(), TODO()) } }
        val behaviourJob = behaviourLogic?.let { f -> launch { f(TODO(), TODO(), TODO(), TODO(), TODO()) } }
        val actuatorsJob = actuatorsLogic?.let { f -> launch { f(TODO(), TODO()) } }
        val sensorsJob = sensorsLogic?.let { f -> launch { f(TODO(), TODO()) } }
        val commJob = communicationLogic?.let { f -> launch { f(TODO(), TODO()) } }
        return@coroutineScope setOf(stateJob, behaviourJob, actuatorsJob, sensorsJob, commJob).mapNotNull { it }.toSet()
    }

    companion object {
        fun <S> PulverizationPlatformScope<S, Nothing, Nothing, Nothing, Nothing>.stateLogic(
            logic: suspend (State<S>, BehaviourRef) -> Unit,
        ) where S : StateRepresentation {
            stateLogic = logic
        }

        fun <AS> PulverizationPlatformScope<Nothing, Nothing, Nothing, AS, Nothing>.actuatorsLogic(
            logic: suspend (ActuatorsContainer, BehaviourRef) -> Unit,
        ) {
            actuatorsLogic = logic
        }

        fun <SS> PulverizationPlatformScope<Nothing, Nothing, SS, Nothing, Nothing>.sensorsLogic(
            logic: suspend (SensorsContainer, BehaviourRef) -> Unit,
        ) {
            sensorsLogic = logic
        }

        fun <C> PulverizationPlatformScope<Nothing, C, Nothing, Nothing, Nothing>.communicationLogic(
            logic: suspend (Communication<C>, BehaviourRef) -> Unit,
        ) where C : CommunicationPayload {
            communicationLogic = logic
        }

        fun <S, C, SS, AS, R> PulverizationPlatformScope<S, C, SS, AS, R>.behaviourLogic(
            logic: suspend (Behaviour<S, C, SS, AS, R>, StateRef, SensorsRef, ActuatorRef, CommunicationRef) -> Unit,
        ) where S : StateRepresentation, C : CommunicationPayload {
            behaviourLogic = logic
        }
    }
}

// class PulverizationRuntime {
//
//    fun withDeploymentUnit(
//        deviceConfig: LogicalDeviceConfiguration,
//        vararg deployableComponents: PulverizedComponentType,
//        init: suspend RuntimeDSL.() -> Unit,
//    ) {
//        if (deviceConfig.deploymentUnits.any { it.deployableComponents.containsAll(deployableComponents.toSet()) }) {
//            error("Unable to find a deployable unit with the provided components: $deployableComponents")
//        }
//        // The following components are remote and for each one of this, a component reference should be produced.
//        val remoteComponents = deviceConfig.components - deployableComponents.toSet()
//
//        deployableComponents.toSet().forEach {
//            when (it) {
//                ActuatorsComponent -> TODO()
//                BehaviourComponent -> TODO()
//                CommunicationComponent -> TODO()
//                SensorsComponent -> TODO()
//                StateComponent -> TODO()
//            }
//        }
//    }
//
//    private fun linkWithBehaviour(
//        self: PulverizedComponent,
//        remotes: Set<PulverizedComponentType>,
//        locals: Set<PulverizedComponentType>,
//    ): BehaviourRef = TODO()
//
//    private fun linkWithOtherComponents(
//        remotes: Set<PulverizedComponentType>,
//        locals: Set<PulverizedComponentType>,
//    ): Quadruple<StateRef?, CommunicationRef?, SensorsRef?, ActuatorRef?> = TODO()
// }
//
// internal data class Quadruple<out A, out B, out C, out D>(val first: A, val second: B, val third: C, val fourth: D)

/* This sample of code shows the high level API to create a pulverized system

data class SR(val i: Int) : StateRepresentation
class MyState(override val context: Context) : State<SR> {
    override fun get(): SR = TODO("Not yet implemented")

    override fun update(newState: SR): SR = TODO("Not yet implemented")
}

suspend fun senseLogic(state: MyState, bhRef: BehaviourRef): Unit = coroutineScope {
    TODO()
}

suspend fun test(): Unit = coroutineScope {
    PulverizationRuntime().withDeploymentUnit(TODO(), StateComponent) {
        withPlatform()
        withStateLogic(::senseLogic)
    }
}
*/
