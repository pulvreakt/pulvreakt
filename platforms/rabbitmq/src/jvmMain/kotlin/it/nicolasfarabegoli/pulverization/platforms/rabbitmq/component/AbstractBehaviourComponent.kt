package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.ComponentsType
import it.nicolasfarabegoli.pulverization.core.DeviceID
import it.nicolasfarabegoli.pulverization.core.Export
import it.nicolasfarabegoli.pulverization.core.LogicalDevice
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.RabbitmqBidirectionalCommunicator
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.RabbitmqReceiverCommunicator
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.RabbitmqSenderCommunicator
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.SimpleRabbitmqBidirectionalCommunication
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.SimpleRabbitmqReceiverCommunicator
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.SimpleRabbitmqSenderCommunicator
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual abstract class AbstractBehaviourComponent<S, E, W, A, I>(
    private val device: LogicalDevice<I>,
    private val stateSer: SerializationStrategy<S>? = null,
    private val stateDeser: DeserializationStrategy<S>? = null,
    private val sensorDeser: DeserializationStrategy<W>? = null,
    private val actuatorsSer: SerializationStrategy<A>? = null,
    private val commSer: SerializationStrategy<E>? = null,
    private val commDeser: DeserializationStrategy<E>? = null,
) : DeviceComponent<I>, KoinComponent
    where S : StateRepresentation, E : Export, I : DeviceID {

    protected val behaviour: Behaviour<S, E, W, A, Unit, I> by inject()
    private var stateComponent: RabbitmqBidirectionalCommunicator<S, S, I>? = null
    private var sensorsComponent: RabbitmqReceiverCommunicator<W, I>? = null
    private var actuatorsComponent: RabbitmqSenderCommunicator<A, I>? = null
    private var communicationComponent: RabbitmqBidirectionalCommunicator<E, E, I>? = null

    init {
        device.components.forEach {
            when (it) {
                ComponentsType.ACTUATORS ->
                    actuatorsSer?.let { ser ->
                        actuatorsComponent = SimpleRabbitmqSenderCommunicator(id, "actuators/$id", ser)
                    } ?: error("The actuators serializer must be provided")

                ComponentsType.BEHAVIOUR -> error("The behaviour must not have a self referencing component")
                ComponentsType.COMMUNICATION ->
                    commSer?.let { ser ->
                        commDeser?.let { deser ->
                            communicationComponent =
                                SimpleRabbitmqBidirectionalCommunication(id, "communications/$id", ser, deser)
                        } ?: error("The communication deserializer must be provided")
                    } ?: error("The communication serializer must be provided")

                ComponentsType.SENSORS ->
                    sensorDeser?.let { ser ->
                        sensorsComponent = SimpleRabbitmqReceiverCommunicator(id, "sensors/$id", ser)
                    } ?: error("The sensors deserialized must be provided")

                ComponentsType.STATE ->
                    stateSer?.let { ser ->
                        stateDeser?.let { deser ->
                            stateComponent = SimpleRabbitmqBidirectionalCommunication(id, "state/$id", ser, deser)
                        } ?: error("The state deserialized must be provided")
                    } ?: error("The state serializer must be provided")
            }
        }
    }

    override val id: I = device.id
}
