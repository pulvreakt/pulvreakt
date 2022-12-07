package it.nicolasfarabegoli.pulverization.runtime.communication

import kotlinx.coroutines.flow.Flow

/**
 * Models the communication between component.
 * Is used to enable intra-component-communication.
 */
interface Communicator {

    /**
     * Setup the communication with a given [binding].
     */
    suspend fun setup(binding: Binding, remotePlace: RemotePlace?)

    /**
     * Release all the communicator's resources, if any.
     */
    suspend fun finalize()

    /**
     * This method send a [message] to a destination component.
     */
    suspend fun fireMessage(message: ByteArray)

    /**
     * This method returns a [Flow] containing all the messages received from the destination component.
     */
    fun receiveMessage(): Flow<ByteArray>
}
