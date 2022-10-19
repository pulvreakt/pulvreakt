package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import it.nicolasfarabegoli.pulverization.communication.BidirectionalCommunicator
import it.nicolasfarabegoli.pulverization.communication.ReceiverCommunicator
import it.nicolasfarabegoli.pulverization.communication.SenderCommunicator
import it.nicolasfarabegoli.pulverization.core.DeviceID

/**
 * Represents a sender-only communicator backed by RabbitMQ as communication technology.
 * The communication is mediated by a [queue].
 */
interface RabbitmqSenderCommunicator<in Send, I : DeviceID> : SenderCommunicator<Send, I> {
    /**
     * The name of the queue in which the communications flows.
     */
    val queue: String
}

/**
 * Represents a receiver-only communicator backed by RabbitMQ as a communication technology.
 * The communication is mediated by a [queue].
 */
interface RabbitmqReceiverCommunicator<out Receive, I : DeviceID> : ReceiverCommunicator<Receive, I> {
    /**
     * The name of the queue in which the communications flows.
     */
    val queue: String
}

/**
 * Represents a bidirectional communication backed by RabbitMQ as a communication technology.
 * The communication is mediated by a [queue].
 */
interface RabbitmqBidirectionalCommunicator<in Send, out Receive, I : DeviceID> :
    BidirectionalCommunicator<Send, Receive, I> {
    /**
     * The name of the queue in which the communications flows.
     */
    val queue: String
}
