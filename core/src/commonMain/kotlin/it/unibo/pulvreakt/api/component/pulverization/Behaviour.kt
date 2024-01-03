package it.unibo.pulvreakt.api.component.pulverization

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.api.component.ComponentKind
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.scheduler.ExecutionScheduler
import it.unibo.pulvreakt.errors.component.ComponentError
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Represents the Behaviour component in the pulverization model.
 */
abstract class Behaviour<State : Any, Comm : Any, in Sensors : Any, out Actuators : Any>(
    private val executionScheduler: ExecutionScheduler,
    private val stateSerializer: KSerializer<StateOps<State>> = serializer(),
    private val commSerializer: KSerializer<CommunicationPayload<Comm>> = serializer(),
    private val sensorsSerializer: KSerializer<Sensors>,
    private val actuatorsSerializer: KSerializer<Actuators>,
) : AbstractPulverizedComponent() {

    private val deviceId by lazy { context.deviceId }
    private val myRef by lazy { ComponentRef.create(this, ComponentKind.Behavior) }

    /**
     * Execute the behaviour logic with the given [state], [comm] and [sensors].
     * @return the new state, the communication to be sent and the prescriptive actions to be performed.
     */
    abstract operator fun invoke(state: State?, comm: List<Comm>, sensors: Sensors?): BehaviourOutput<State, Comm, Actuators>

    final override fun getRef(): ComponentRef = myRef

    override suspend fun execute(): Either<ComponentError, Unit> = coroutineScope {
        either {
            val stateRef = getComponentByTypeOrNull(ComponentKind.State)
            val commRef = getComponentByTypeOrNull(ComponentKind.Communication)
            val sensorsRef = getComponentByTypeOrNull(ComponentKind.Sensor)
            val actuatorsRef = getComponentByTypeOrNull(ComponentKind.Actuator)

            var receivedNeighboursMessages = listOf<CommunicationPayload<Comm>>()
            var lastSensorsRead: Sensors? = null

            val commJob = executeIfNotNull(commRef) { ref ->
                launch {
                    val communicationFlow = receive(ref, commSerializer).bind()
                    communicationFlow.collect {
                        receivedNeighboursMessages = receivedNeighboursMessages.filter { message -> message.deviceId != message.deviceId } + it
                    }
                }
            }

            val sensorsJob = executeIfNotNull(sensorsRef) { ref ->
                launch { receive(ref, sensorsSerializer).bind().collect { lastSensorsRead = it } }
            }

            executionScheduler.timesSequence().forEach { time ->
                val stateContent = executeIfNotNull(stateRef) { ref ->
                    send(ref, GetState, stateSerializer).bind()
                    when (val state = receive(ref, stateSerializer).bind().first()) {
                        is SetState -> state.content
                        is GetState -> null
                    }
                }

                val (newState, newComm, newActuators) = invoke(
                    stateContent,
                    receivedNeighboursMessages.map { it.payload },
                    lastSensorsRead,
                )

                executeIfNotNull(newState, stateRef) { state, ref ->
                    send(ref, SetState(state), stateSerializer).bind()
                }
                executeIfNotNull(newComm, commRef) { comm, ref ->
                    send(ref, CommunicationPayload(deviceId, comm), commSerializer).bind()
                }
                executeIfNotNull(newActuators, actuatorsRef) { actuators, ref ->
                    send(ref, actuators, actuatorsSerializer).bind()
                }

                delay(time)
            }
            commJob?.cancelAndJoin()
            sensorsJob?.cancelAndJoin()
        }
    }

    /**
     * Executes the given [block] if the given [value] is not null.
     * @return the result of the block execution or null if the value is null.
     */
    private suspend fun <T, P> executeIfNotNull(value: T?, block: suspend (T) -> P): P? = value?.let { block(it) }

    /**
     * Executes the given [block] if the given [value1] and [value2] are not null.
     * @return the result of the block execution or null if one of the values is null.
     */
    private suspend fun <T, R, P> executeIfNotNull(value1: T?, value2: R?, block: suspend (T, R) -> P): P? =
        value1?.let { v1 -> value2?.let { v2 -> block(v1, v2) } }
}

/**
 * Models the output of a [Behaviour] where [state] is the new state of the component,
 * [comm] is the communication to be sent to the other devices
 * and [actuators] is the prescriptive actions to be performed.
 */
data class BehaviourOutput<out State : Any, out Comm : Any, out Actuators : Any>(
    val state: State?,
    val comm: Comm?,
    val actuators: Actuators?,
)
