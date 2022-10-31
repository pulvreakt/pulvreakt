package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import org.koin.core.component.KoinComponent
import kotlin.reflect.KClass

/**
 * Simple implementation for communicate (send only) with another component using RabbitMQ.
 */
expect class SimpleRabbitmqSenderCommunicator<Send : Any>(
    type: KClass<Send>,
    exchange: String,
    queue: String,
) : RabbitmqSenderCommunicator<Send>, KoinComponent {
    companion object {
        /**
         * Creates the class without specifying the KClass of the payload.
         */
        inline operator fun <reified S : Any> invoke(
            exchange: String,
            queue: String,
        ): SimpleRabbitmqSenderCommunicator<S>
    }

    override val context: RabbitmqContext
}

/**
 * Simple implementation for communicate (receive only) with another component using RabbitMQ.
 */
expect class SimpleRabbitmqReceiverCommunicator<Receive : Any>(
    type: KClass<Receive>,
    exchange: String,
    queue: String,
) : RabbitmqReceiverCommunicator<Receive>, KoinComponent {
    companion object {
        /**
         * Creates the class without specifying the KClass of the payload.
         */
        inline operator fun <reified R : Any> invoke(
            exchange: String,
            queue: String,
        ): SimpleRabbitmqReceiverCommunicator<R>
    }

    override val context: RabbitmqContext
}

/**
 * Simple implementation for communicate with another component using RabbitMQ.
 */
expect class SimpleRabbitmqBidirectionalCommunication<Send : Any, Receive : Any>(
    kSend: KClass<Send>,
    kReceive: KClass<Receive>,
    exchange: String,
    queue: String,
) : RabbitmqBidirectionalCommunicator<Send, Receive>, KoinComponent {
    companion object {
        /**
         * Creates the class without specifying the KClass of the payloads.
         */
        inline operator fun <reified S : Any, reified R : Any> invoke(
            exchange: String,
            queue: String,
        ): SimpleRabbitmqBidirectionalCommunication<S, R>
    }

    override val context: RabbitmqContext
}
