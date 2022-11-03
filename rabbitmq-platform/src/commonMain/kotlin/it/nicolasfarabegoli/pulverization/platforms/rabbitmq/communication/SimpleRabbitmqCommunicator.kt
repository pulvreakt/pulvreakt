package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import org.koin.core.component.KoinComponent
import kotlin.reflect.KClass

/**
 * Simple implementation for communicate (send only) with another component using RabbitMQ.
 */
expect class SimpleRabbitmqSenderCommunicator<Send : Any>(
    type: KClass<Send>,
    communicationType: Pair<PulverizedComponentType, PulverizedComponentType>,
) : RabbitmqSenderCommunicator<Send>, KoinComponent {
    companion object {
        /**
         * Creates the class without specifying the KClass of the payload.
         */
        inline operator fun <reified S : Any> invoke(
            communicationType: Pair<PulverizedComponentType, PulverizedComponentType>,
        ): SimpleRabbitmqSenderCommunicator<S>
    }

    override val context: RabbitmqContext
}

/**
 * Simple implementation for communicate (receive only) with another component using RabbitMQ.
 */
expect class SimpleRabbitmqReceiverCommunicator<Receive : Any>(
    type: KClass<Receive>,
    communicationType: Pair<PulverizedComponentType, PulverizedComponentType>,
) : RabbitmqReceiverCommunicator<Receive>, KoinComponent {
    companion object {
        /**
         * Creates the class without specifying the KClass of the payload.
         */
        inline operator fun <reified R : Any> invoke(
            communicationType: Pair<PulverizedComponentType, PulverizedComponentType>,
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
    communicationType: Pair<PulverizedComponentType, PulverizedComponentType>,
) : RabbitmqBidirectionalCommunicator<Send, Receive>, KoinComponent {
    companion object {
        /**
         * Creates the class without specifying the KClass of the payloads.
         */
        inline operator fun <reified S : Any, reified R : Any> invoke(
            communicationType: Pair<PulverizedComponentType, PulverizedComponentType>,
        ): SimpleRabbitmqBidirectionalCommunication<S, R>
    }

    override val context: RabbitmqContext
}
