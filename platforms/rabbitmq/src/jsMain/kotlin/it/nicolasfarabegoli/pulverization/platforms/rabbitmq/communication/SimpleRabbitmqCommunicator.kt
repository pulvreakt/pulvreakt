package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KClass

/**
 * Simple implementation for communicate (send only) with another component using RabbitMQ.
 */
actual class SimpleRabbitmqSenderCommunicator<Send : Any> actual constructor(
    type: KClass<Send>,
    override val exchange: String,
    override val queue: String,
) : RabbitmqSenderCommunicator<Send>, KoinComponent {
    actual override val context: RabbitmqContext by inject()

    actual companion object {
        /**
         * Creates the communication without the need of passing the KClass.
         */
        actual inline operator fun <reified S : Any> invoke(exchange: String, queue: String) =
            SimpleRabbitmqSenderCommunicator(S::class, exchange, queue)
    }

    override suspend fun sendToComponent(payload: Send) {
        TODO("Not yet implemented")
    }
}

/**
 * Simple implementation for communicate (receive only) with another component using RabbitMQ.
 */
actual class SimpleRabbitmqReceiverCommunicator<Receive : Any> actual constructor(
    type: KClass<Receive>,
    override val exchange: String,
    override val queue: String,
) : RabbitmqReceiverCommunicator<Receive>, KoinComponent {
    actual override val context: RabbitmqContext by inject()

    actual companion object {
        /**
         * Creates the class without specifying the KClass of the payload.
         */
        actual inline operator fun <reified R : Any> invoke(
            exchange: String,
            queue: String,
        ): SimpleRabbitmqReceiverCommunicator<R> = SimpleRabbitmqReceiverCommunicator(R::class, exchange, queue)
    }

    override fun receiveFromComponent(): Flow<Receive> {
        TODO("Not yet implemented")
    }
}

/**
 * Simple implementation for communicate with another component using RabbitMQ.
 */
actual class SimpleRabbitmqBidirectionalCommunication<Send : Any, Receive : Any> actual constructor(
    kSend: KClass<Send>,
    kReceive: KClass<Receive>,
    override val exchange: String,
    override val queue: String,
) : RabbitmqBidirectionalCommunicator<Send, Receive>, KoinComponent {
    actual override val context: RabbitmqContext by inject()

    override suspend fun sendToComponent(payload: Send) {
        TODO("Not yet implemented")
    }

    override fun receiveFromComponent(): Flow<Receive> {
        TODO("Not yet implemented")
    }

    actual companion object {
        /**
         * Creates the class without specifying the KClass of the payload.
         */
        actual inline operator fun <reified S : Any, reified R : Any> invoke(
            exchange: String,
            queue: String,
        ): SimpleRabbitmqBidirectionalCommunication<S, R> =
            SimpleRabbitmqBidirectionalCommunication(S::class, R::class, exchange, queue)
    }
}
