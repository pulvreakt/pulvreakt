package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import it.nicolasfarabegoli.pulverization.core.DeviceID
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import kotlin.reflect.KClass

/**
 * Simple implementation for communicate (send only) with another component using RabbitMQ.
 */
actual class SimpleRabbitmqSenderCommunicator<Send : Any, I : DeviceID> actual constructor(
    type: KClass<Send>,
    override val id: I,
    override val queue: String,
) : RabbitmqSenderCommunicator<Send, I>, KoinComponent {
    override suspend fun sendToComponent(payload: Send) {
        TODO("Not yet implemented")
    }

    actual companion object {
        actual inline operator fun <reified S : Any> invoke(
            id: DeviceID,
            queue: String,
        ): SimpleRabbitmqSenderCommunicator<S, DeviceID> = SimpleRabbitmqSenderCommunicator(S::class, id, queue)
    }
}

/**
 * Simple implementation for communicate (receive only) with another component using RabbitMQ.
 */
actual class SimpleRabbitmqReceiverCommunicator<Receive : Any, I : DeviceID> actual constructor(
    type: KClass<Receive>,
    override val id: I,
    override val queue: String,
) : RabbitmqReceiverCommunicator<Receive, I>, KoinComponent {
    override fun receiveFromComponent(): Flow<Receive> {
        TODO("Not yet implemented")
    }

    actual companion object {
        actual inline operator fun <reified R : Any> invoke(
            id: DeviceID,
            queue: String,
        ): SimpleRabbitmqReceiverCommunicator<R, DeviceID> = SimpleRabbitmqReceiverCommunicator(R::class, id, queue)
    }
}

/**
 * Simple implementation for communicate with another component using RabbitMQ.
 */
actual class SimpleRabbitmqBidirectionalCommunication<Send : Any, Receive : Any, I : DeviceID> actual constructor(
    kSend: KClass<Send>,
    kReceive: KClass<Receive>,
    override val id: I,
    override val queue: String,
) : RabbitmqBidirectionalCommunicator<Send, Receive, I>, KoinComponent {
    override suspend fun sendToComponent(payload: Send) {
        TODO("Not yet implemented")
    }

    override fun receiveFromComponent(): Flow<Receive> {
        TODO("Not yet implemented")
    }

    actual companion object {
        actual inline operator fun <reified S : Any, reified R : Any> invoke(
            id: DeviceID,
            queue: String,
        ): SimpleRabbitmqBidirectionalCommunication<S, R, DeviceID> =
            SimpleRabbitmqBidirectionalCommunication(S::class, R::class, id, queue)
    }
}
