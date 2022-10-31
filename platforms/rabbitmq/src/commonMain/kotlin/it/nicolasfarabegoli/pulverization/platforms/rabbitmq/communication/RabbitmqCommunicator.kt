package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import it.nicolasfarabegoli.pulverization.communication.BidirectionalCommunicator
import it.nicolasfarabegoli.pulverization.communication.ReceiverCommunicator
import it.nicolasfarabegoli.pulverization.communication.SenderCommunicator
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext

/**
 * Add rabbitmq specific stuff.
 */
interface RabbitmqCommunicator {
    /**
     * The name of the queue in which the communications flows.
     */
    val queue: String

    /**
     * The exchange name.
     */
    val exchange: String

    /**
     * Initialize the communicator component.
     */
    suspend fun initialize() {}

    /**
     * Release all the resources of the communicator component.
     */
    suspend fun finalize() {}
}

/**
 * Represents a sender-only communicator backed by RabbitMQ as communication technology.
 * The communication is mediated by a [queue].
 */
interface RabbitmqSenderCommunicator<Send : Any> : SenderCommunicator<Send, RabbitmqContext>, RabbitmqCommunicator

/**
 * Represents a receiver-only communicator backed by RabbitMQ as a communication technology.
 */
interface RabbitmqReceiverCommunicator<Receive : Any> :
    ReceiverCommunicator<Receive, RabbitmqContext>, RabbitmqCommunicator

/**
 * Represents a bidirectional communication backed by RabbitMQ as a communication technology.
 * The communication is mediated by a [queue].
 */
interface RabbitmqBidirectionalCommunicator<in Send, out Receive> :
    BidirectionalCommunicator<Send, Receive, RabbitmqContext>, RabbitmqCommunicator
