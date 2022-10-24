package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import it.nicolasfarabegoli.pulverization.core.DeviceID
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

/**
 * Simple implementation for communicate (send only) with another component using RabbitMQ.
 */
actual class SimpleRabbitmqSenderCommunicator<Send : Any, I : DeviceID>(
    override val id: I,
    override val queue: String,
) : RabbitmqSenderCommunicator<Send, I>, KoinComponent {
    override suspend fun sendToComponent(payload: Send) {
        TODO("Not yet implemented")
    }
}

/**
 * Simple implementation for communicate (receive only) with another component using RabbitMQ.
 */
actual class SimpleRabbitmqReceiverCommunicator<Receive : Any, I : DeviceID>(
    override val id: I,
    override val queue: String,
) : RabbitmqReceiverCommunicator<Receive, I>, KoinComponent {
    override fun receiveFromComponent(): Flow<Receive> {
        TODO("Not yet implemented")
    }
}

/**
 * Simple implementation for communicate with another component using RabbitMQ.
 */
actual class SimpleRabbitmqBidirectionalCommunication<in Send, out Receive, I : DeviceID>(
    override val id: I,
    override val queue: String,
) : RabbitmqBidirectionalCommunicator<Send, Receive, I>, KoinComponent {
    override suspend fun sendToComponent(payload: Send) {
        TODO("Not yet implemented")
    }

    override fun receiveFromComponent(): Flow<Receive> {
        TODO("Not yet implemented")
    }
}
