package it.nicolasfarabegoli.pulverization.communication

/**
 * Models the ability to (only) send messages to other components.
 */
interface SenderCommunicator<in P, in T> {
    fun sendToComponent(payload: P, to: T)
}

/**
 * Models the ability to (only) receive messages from other components.
 */
interface ReceiverCommunicator<out P, in T> {
    fun receiveFromComponent(from: T): P
}

/**
 * Models the ability to send and receive messages to and from other components.
 */
interface ComponentCommunicatorBidirectional<in PS, out PR, T> : SenderCommunicator<PS, T>, ReceiverCommunicator<PR, T>
