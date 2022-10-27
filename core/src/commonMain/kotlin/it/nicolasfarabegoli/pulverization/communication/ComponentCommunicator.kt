package it.nicolasfarabegoli.pulverization.communication

import it.nicolasfarabegoli.pulverization.component.Context
import kotlinx.coroutines.flow.Flow

interface Communicator<C : Context> {
    val context: C
}

/**
 * Models the ability to (only) send messages to other components.
 */
interface SenderCommunicator<in Send, C : Context> : Communicator<C> {
    /**
     * Send the [payload] [to] another component.
     */
    suspend fun sendToComponent(payload: Send)
}

/**
 * Models the ability to (only) receive messages from other components.
 */
interface ReceiverCommunicator<out Receive, C : Context> : Communicator<C> {
    /**
     * Creates an async flow with all received messages.
     */
    fun receiveFromComponent(): Flow<Receive>
}

/**
 * Models the ability to send and receive messages to and from other components.
 */
interface BidirectionalCommunicator<in Send, out Receive, C : Context> :
    SenderCommunicator<Send, C>, ReceiverCommunicator<Receive, C>
