package it.nicolasfarabegoli.pulverization.communication

/**
 * Models the ability to (only) send messages to other components.
 */
interface SenderCommunicator<in Send, in I> {
    fun sendToComponent(payload: Send, to: I? = null)
}

/**
 * Models the ability to (only) receive messages from other components.
 */
interface ReceiverCommunicator<out Receive, in I> {
    fun receiveFromComponent(from: I? = null): Receive
}

/**
 * Models the ability to send and receive messages to and from other components.
 */
interface BidirectionalCommunicator<in Send, out Receive, I> : SenderCommunicator<Send, I>, ReceiverCommunicator<Receive, I>
