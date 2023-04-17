package it.nicolasfarabegoli.pulverization.runtime.communication

import it.nicolasfarabegoli.pulverization.dsl.model.ComponentType
import kotlinx.coroutines.flow.Flow

typealias Binding = Pair<ComponentType, ComponentType>

/**
 * Models the communication between component.
 * Is used to enable intra-component-communication.
 */
interface Communicator {

    /**
     * Return the remote place provider belonging to this communicator.
     */
    val remotePlaceProvider: RemotePlaceProvider

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
