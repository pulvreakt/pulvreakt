package it.nicolasfarabegoli.pulverization.communication

/**
 * Models the ability to (only) send messages to other components.
 */
interface SenderCommunicator<in P, in I> {
    fun sendToComponent(payload: P, to: I)
}

/**
 * Models the ability to (only) receive messages from other components.
 */
interface ReceiverCommunicator<out P, in I> {
    fun receiveFromComponent(from: I): P
}

/**
 * Models the ability to send and receive messages to and from other components.
 */
interface BidirectionalCommunicator<in PS, out PR, I> : SenderCommunicator<PS, I>, ReceiverCommunicator<PR, I>
