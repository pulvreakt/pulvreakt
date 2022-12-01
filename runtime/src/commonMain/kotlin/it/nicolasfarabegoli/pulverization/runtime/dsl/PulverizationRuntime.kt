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
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

typealias StateLogicType<S> = suspend (State<S>, BehaviourRef<S>) -> Unit
typealias ActuatorsLogicType<AS> = suspend (ActuatorsContainer, BehaviourRef<AS>) -> Unit
typealias SensorsLogicType<SS> = suspend (SensorsContainer, BehaviourRef<SS>) -> Unit
typealias CommunicationLogicType<C> = suspend (Communication<C>, BehaviourRef<C>) -> Unit
typealias BehaviourLogicType<S, C, SS, AS, R> =
    suspend (Behaviour<S, C, SS, AS, R>, StateRef<S>, CommunicationRef<C>, SensorsRef<SS>, ActuatorsRef<AS>) -> Unit

inline fun <reified S, reified C, reified SS, reified AS, reified R> pulverizationPlatform(
    config: LogicalDeviceConfiguration,
    init: PulverizationPlatformScope<S, C, SS, AS, R>.() -> Unit,
) where S : StateRepresentation, C : CommunicationPayload, SS : Any, AS : Any, R : Any =
    PulverizationPlatformScope<S, C, SS, AS, R>(serializer(), serializer(), serializer(), serializer(), config)
        .apply(init)

class PulverizationPlatformScope<S, C, SS : Any, AS : Any, R : Any>(
    private val stateType: KSerializer<S>,
    private val commType: KSerializer<C>,
    private val sensorsType: KSerializer<SS>,
    private val actuatorsType: KSerializer<AS>,
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
        val (sr, cm, ss, act) =
            setupComponentsRef(stateType, commType, sensorsType, actuatorsType, emptySet(), emptySet())
        // Initialize all Component(s)Ref
        setOf(sr, cm, ss, act).forEach { it.setup() }

        val behaviourJob = behaviourLogic to behaviourComponent takeAllNotNull { logic, comp ->
            launch { logic(comp, sr, cm, ss, act) }
        }
        TODO()
//        val communicationJob = communicationLogic to communicationComponent takeAllNotNull { logic, comp ->
//            launch { logic(comp, TODO()) }
//        }
//        val actuatorsJob = actuatorsLogic to actuatorsComponent takeAllNotNull { logic, comp ->
//            launch { logic(comp, TODO()) }
//        }
//        val sensorsJob = sensorsLogic to sensorsComponent takeAllNotNull { logic, comp ->
//            launch { logic(comp, TODO()) }
//        }
//        val stateJob = stateLogic to stateComponent takeAllNotNull { logic, comp ->
//            launch { logic(comp, TODO()) }
//        }
//        setOf(stateJob, behaviourJob, actuatorsJob, sensorsJob, communicationJob).filterNotNull().toSet()
    }

    companion object {
        fun <S, C, SS, AS, R> PulverizationPlatformScope<S, C, SS, AS, R>.behaviourLogic(
            behaviour: Behaviour<S, C, SS, AS, R>,
            logic: BehaviourLogicType<S, C, SS, AS, R>,
        ) where S : StateRepresentation, C : CommunicationPayload, SS : Any, AS : Any, R : Any {
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

        fun <AS : Any> PulverizationPlatformScope<Nothing, Nothing, Nothing, AS, Nothing>.actuatorsLogic(
            actuators: ActuatorsContainer,
            logic: ActuatorsLogicType<AS>,
        ) {
            actuatorsComponent = actuators
            actuatorsLogic = logic
        }

        fun <SS : Any> PulverizationPlatformScope<Nothing, Nothing, SS, Nothing, Nothing>.sensorsLogic(
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
