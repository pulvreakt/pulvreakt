package it.unibo.pulvreakt.core.component.pulverisation

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.component.errors.ComponentError
import it.unibo.pulvreakt.core.component.time.TimeDistribution
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer

/**
 * Represents the Behaviour component in the pulverization model.
 */
abstract class Behaviour<State : Any, Comm : Any, Sensors : Any, Actuators : Any>(
    private val timeDistribution: TimeDistribution,
    private val stateSerializer: KSerializer<StateOps<State>>,
    private val commSerializer: KSerializer<CommunicationPayload<Comm>>,
    private val sensorsSerializer: KSerializer<Sensors>,
    private val actuatorsSerializer: KSerializer<Actuators>,
) : AbstractPulverisedComponent() {

    private val deviceId by lazy { context.deviceId }

    /**
     * Execute the behaviour logic with the given [state], [comm] and [sensors].
     * @return the new state, the communication to be sent and the prescriptive actions to be performed.
     */
    abstract operator fun invoke(state: State, comm: List<Comm>, sensors: Sensors): BehaviourOutput<State, Comm, Actuators>

    final override fun getRef(): ComponentRef = ComponentRef.create(this, ComponentType.Behaviour)

    override suspend fun execute(): Either<ComponentError, Unit> = coroutineScope {
        either {
            val stateRef = getComponentByType(ComponentType.State).bind()
            val commRef = getComponentByType(ComponentType.Communication).bind()
            val sensorsRef = getComponentByType(ComponentType.Sensor).bind()
            val actuatorsRef = getComponentByType(ComponentType.Actuator).bind()

            var receivedNeighboursMessages = listOf<CommunicationPayload<Comm>>()
            var lastSensorsRead: Sensors? = null

            val commJob = launch {
                val communicationFlow = receive(commRef, commSerializer).bind()
                communicationFlow.collect {
                    receivedNeighboursMessages = receivedNeighboursMessages.filter { message -> message.deviceId != message.deviceId } + it
                }
            }

            val sensorsJob = launch {
                val sensorsFlow = receive(sensorsRef, sensorsSerializer).bind()
                sensorsFlow.collect { lastSensorsRead = it }
            }

            // Loop with a user-specific policy
            while (!timeDistribution.isCompleted()) {
                send(stateRef, StateOps.GetState(null), stateSerializer).bind()
                when (val state = receive(stateRef, stateSerializer).bind().first()) {
                    is StateOps.SetState -> {
                        val (newState, newComm, newActuators) = invoke(
                            state.content,
                            receivedNeighboursMessages.map { it.payload },
                            lastSensorsRead!!,
                        )
                        send(stateRef, StateOps.SetState(newState), stateSerializer).bind()
                        send(commRef, CommunicationPayload(deviceId, newComm), commSerializer).bind()
                        send(actuatorsRef, newActuators, actuatorsSerializer).bind()
                    }

                    is StateOps.GetState<*, *> -> Unit
                }
                delay(timeDistribution.nextTimeInstant())
            }

            commJob.join()
            sensorsJob.join()
        }
    }
}

/**
 * Models the output of a [Behaviour] where [state] is the new state of the component,
 * [comm] is the communication to be sent to the other devices
 * and [actuators] is the prescriptive actions to be performed.
 */
data class BehaviourOutput<State : Any, Comm : Any, Actuators : Any>(
    val state: State,
    val comm: Comm,
    val actuators: Actuators,
)
