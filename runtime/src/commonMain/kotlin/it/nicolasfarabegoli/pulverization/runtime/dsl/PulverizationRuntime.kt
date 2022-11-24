package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.dsl.LogicalDeviceConfiguration
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorRef
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
    suspend (Behaviour<S, C, SS, AS, R>, StateRef<S>, CommunicationRef<C>, SensorsRef<SS>, ActuatorRef<AS>) -> Unit

fun <S, C, SS, AS, R> pulverizationPlatform(init: PulverizationPlatformScope<S, C, SS, AS, R>.() -> Unit): Nothing
    where S : StateRepresentation, C : CommunicationPayload = TODO()

class PulverizationPlatformScope<S, C, SS, AS, R>(
    private val deviceConfig: LogicalDeviceConfiguration,
) where S : StateRepresentation, C : CommunicationPayload {

    var stateLogic: StateLogicType<S>? = null
    var actuatorsLogic: ActuatorsLogicType<AS>? = null
    var sensorsLogic: SensorsLogicType<SS>? = null
    var communicationLogic: CommunicationLogicType<C>? = null
    var behaviourLogic: BehaviourLogicType<S, C, SS, AS, R>? = null

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
            logic: StateLogicType<S>,
        ) where S : StateRepresentation {
            stateLogic = logic
        }

        fun <AS> PulverizationPlatformScope<Nothing, Nothing, Nothing, AS, Nothing>.actuatorsLogic(
            logic: ActuatorsLogicType<AS>,
        ) {
            actuatorsLogic = logic
        }

        fun <SS> PulverizationPlatformScope<Nothing, Nothing, SS, Nothing, Nothing>.sensorsLogic(
            logic: SensorsLogicType<SS>,
        ) {
            sensorsLogic = logic
        }

        fun <C> PulverizationPlatformScope<Nothing, C, Nothing, Nothing, Nothing>.communicationLogic(
            logic: CommunicationLogicType<C>,
        ) where C : CommunicationPayload {
            communicationLogic = logic
        }

        fun <S, C, SS, AS, R> PulverizationPlatformScope<S, C, SS, AS, R>.behaviourLogic(
            logic: BehaviourLogicType<S, C, SS, AS, R>,
        ) where S : StateRepresentation, C : CommunicationPayload {
            behaviourLogic = logic
        }
    }
}
