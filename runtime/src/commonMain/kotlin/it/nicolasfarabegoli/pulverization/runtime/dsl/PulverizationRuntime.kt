package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.dsl.LogicalDeviceConfiguration
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

typealias StateLogicType<S> = suspend (State<S>, BehaviourRef<S>) -> Unit
typealias ActuatorsLogicType<AS> = suspend (ActuatorsContainer, BehaviourRef<AS>) -> Unit
typealias SensorsLogicType<SS> = suspend (SensorsContainer, BehaviourRef<SS>) -> Unit
typealias CommunicationLogicType<C> = suspend (Communication<C>, BehaviourRef<C>) -> Unit
typealias BehaviourLogicType<S, C, SS, AS, R> =
    suspend (Behaviour<S, C, SS, AS, R>, StateRef<S>, CommunicationRef<C>, SensorsRef<SS>, ActuatorsRef<AS>) -> Unit

fun <S, C, SS, AS, R> pulverizationPlatform(init: PulverizationPlatformScope<S, C, SS, AS, R>.() -> Unit): Nothing
    where S : StateRepresentation, C : CommunicationPayload = TODO()

class PulverizationPlatformScope<S, C, SS, AS, R>(
    private val deviceConfig: LogicalDeviceConfiguration,
) where S : StateRepresentation, C : CommunicationPayload {

    var behaviourLogic: BehaviourLogicType<S, C, SS, AS, R>? = null
    var communicationLogic: CommunicationLogicType<C>? = null
    var actuatorsLogic: ActuatorsLogicType<AS>? = null
    var sensorsLogic: SensorsLogicType<SS>? = null
    var stateLogic: StateLogicType<S>? = null

    var behaviourComponent: Behaviour<S, C, SS, AS, R>? = null
    var communicationComponent: Communication<C>? = null
    var actuatorsComponent: ActuatorsContainer? = null
    var sensorsComponent: SensorsContainer? = null
    var stateComponent: State<S>? = null

    suspend fun start(): Set<Job> = coroutineScope {
        val behaviourJob = behaviourLogic to behaviourComponent takeAllNotNull { logic, comp ->
            launch { logic(comp, StateRef(), CommunicationRef(), SensorsRef(), ActuatorsRef()) }
        }
        val communicationJob = communicationLogic to communicationComponent takeAllNotNull { logic, comp ->
            launch { logic(comp, BehaviourRef(comp.componentType)) }
        }
        val actuatorsJob = actuatorsLogic to actuatorsComponent takeAllNotNull { logic, comp ->
            launch { logic(comp, BehaviourRef(comp.componentType)) }
        }
        val sensorsJob = sensorsLogic to sensorsComponent takeAllNotNull { logic, comp ->
            launch { logic(comp, BehaviourRef(comp.componentType)) }
        }
        val stateJob = stateLogic to stateComponent takeAllNotNull { logic, comp ->
            launch { logic(comp, BehaviourRef(comp.componentType)) }
        }
        setOf(stateJob, behaviourJob, actuatorsJob, sensorsJob, communicationJob).filterNotNull().toSet()
    }

    companion object {
        fun <S, C, SS, AS, R> PulverizationPlatformScope<S, C, SS, AS, R>.behaviourLogic(
            behaviour: Behaviour<S, C, SS, AS, R>,
            logic: BehaviourLogicType<S, C, SS, AS, R>,
        ) where S : StateRepresentation, C : CommunicationPayload {
            behaviourComponent = behaviour
            behaviourLogic = logic
        }

        fun <C> PulverizationPlatformScope<Nothing, C, Nothing, Nothing, Nothing>.communicationLogic(
            communication: Communication<C>,
            logic: CommunicationLogicType<C>,
        ) where C : CommunicationPayload {
            communicationComponent = communication
            communicationLogic = logic
        }

        fun <AS> PulverizationPlatformScope<Nothing, Nothing, Nothing, AS, Nothing>.actuatorsLogic(
            actuators: ActuatorsContainer,
            logic: ActuatorsLogicType<AS>,
        ) {
            actuatorsComponent = actuators
            actuatorsLogic = logic
        }

        fun <SS> PulverizationPlatformScope<Nothing, Nothing, SS, Nothing, Nothing>.sensorsLogic(
            sensors: SensorsContainer,
            logic: SensorsLogicType<SS>,
        ) {
            sensorsComponent = sensors
            sensorsLogic = logic
        }

        fun <S> PulverizationPlatformScope<S, Nothing, Nothing, Nothing, Nothing>.stateLogic(
            state: State<S>,
            logic: StateLogicType<S>,
        ) where S : StateRepresentation {
            stateComponent = state
            stateLogic = logic
        }
    }
}

internal infix fun <F, S, R> Pair<F?, S?>.takeAllNotNull(body: (F, S) -> R): R? =
    if (first != null && second != null) body(first!!, second!!) else null
