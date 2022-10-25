package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import it.nicolasfarabegoli.pulverization.core.DeviceID
import org.koin.core.component.KoinComponent
import kotlin.reflect.KClass

/**
 * Simple implementation for communicate (send only) with another component using RabbitMQ.
 */
expect class SimpleRabbitmqSenderCommunicator<Send : Any, I : DeviceID>(
    type: KClass<Send>,
    id: I,
    queue: String,
) : RabbitmqSenderCommunicator<Send, I>,
    KoinComponent {

    companion object {
        inline operator fun <reified S : Any> invoke(
            id: DeviceID,
            queue: String,
        ): SimpleRabbitmqSenderCommunicator<S, DeviceID>
    }
}

/**
 * Simple implementation for communicate (receive only) with another component using RabbitMQ.
 */
expect class SimpleRabbitmqReceiverCommunicator<Receive : Any, I : DeviceID>(
    type: KClass<Receive>,
    id: I,
    queue: String,
) : RabbitmqReceiverCommunicator<Receive, I>,
    KoinComponent {

    companion object {
        inline operator fun <reified R : Any> invoke(
            id: DeviceID,
            queue: String,
        ): SimpleRabbitmqReceiverCommunicator<R, DeviceID>
    }
}

/**
 * Simple implementation for communicate with another component using RabbitMQ.
 */
expect class SimpleRabbitmqBidirectionalCommunication<Send : Any, Receive : Any, I : DeviceID>(
    kSend: KClass<Send>,
    kReceive: KClass<Receive>,
    id: I,
    queue: String,
) : RabbitmqBidirectionalCommunicator<Send, Receive, I>,
    KoinComponent {

    companion object {
        inline operator fun <reified S : Any, reified R : Any> invoke(
            id: DeviceID,
            queue: String,
        ): SimpleRabbitmqBidirectionalCommunication<S, R, DeviceID>
    }
}
