package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import it.nicolasfarabegoli.pulverization.core.DeviceID
import org.koin.core.component.KoinComponent

/**
 * Simple implementation for communicate (send only) with another component using RabbitMQ.
 */
expect class SimpleRabbitmqSenderCommunicator<in Send, I : DeviceID> :
    RabbitmqSenderCommunicator<Send, I>,
    KoinComponent

/**
 * Simple implementation for communicate (receive only) with another component using RabbitMQ.
 */
expect class SimpleRabbitmqReceiverCommunicator<out Receive, I : DeviceID> :
    RabbitmqReceiverCommunicator<Receive, I>,
    KoinComponent

/**
 * Simple implementation for communicate with another component using RabbitMQ.
 */
expect class SimpleRabbitmqBidirectionalCommunication<in Send, out Receive, I : DeviceID> :
    RabbitmqBidirectionalCommunicator<Send, Receive, I>
