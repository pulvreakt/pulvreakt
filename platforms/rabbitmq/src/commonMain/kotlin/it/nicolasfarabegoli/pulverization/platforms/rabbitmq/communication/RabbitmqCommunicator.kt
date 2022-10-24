package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import it.nicolasfarabegoli.pulverization.communication.BidirectionalCommunicator
import it.nicolasfarabegoli.pulverization.communication.ReceiverCommunicator
import it.nicolasfarabegoli.pulverization.communication.SenderCommunicator
import it.nicolasfarabegoli.pulverization.core.DeviceID

/**
 * Add rabbitmq specific stuff.
 */
interface RabbitmqCommunicator {
    /**
     * The name of the queue in which the communications flows.
     */
    val queue: String

    /**
     * Initialize the communicator component.
     */
    suspend fun initialize() {}
}

/**
 * Represents a sender-only communicator backed by RabbitMQ as communication technology.
 * The communication is mediated by a [queue].
 */
interface RabbitmqSenderCommunicator<Send : Any, I : DeviceID> : SenderCommunicator<Send, I>, RabbitmqCommunicator

/**
 * Represents a receiver-only communicator backed by RabbitMQ as a communication technology.
 * The communication is mediated by a [queue].
 */
interface RabbitmqReceiverCommunicator<Receive : Any, I : DeviceID> :
    ReceiverCommunicator<Receive, I>, RabbitmqCommunicator

/**
 * Represents a bidirectional communication backed by RabbitMQ as a communication technology.
 * The communication is mediated by a [queue].
 */
interface RabbitmqBidirectionalCommunicator<in Send, out Receive, I : DeviceID> :
    BidirectionalCommunicator<Send, Receive, I>, RabbitmqCommunicator
