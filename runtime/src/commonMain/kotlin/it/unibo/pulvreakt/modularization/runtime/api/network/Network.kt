package it.unibo.pulvreakt.modularization.runtime.api.network

import arrow.core.Either
import it.unibo.pulvreakt.modularization.api.utils.ManagedResource
import it.unibo.pulvreakt.modularization.runtime.api.network.protocol.Message
import it.unibo.pulvreakt.modularization.runtime.errors.network.NetworkError
import kotlinx.coroutines.flow.Flow

typealias NetworkResult<Result> = Either<NetworkError, Result>

/**
 * The Network interface is responsible for managing the network.
 * The [send] method is used to send a [Message] to the network.
 * The [unprocessedMessages] method is used to retrieve the messages that have not been processed yet.
 * The [messagesFlow] flow emits the messages received from the network in asynchronous way.
 */
interface Network : ManagedResource<NetworkError> {
    /**
     * Sends a [message] to the network.
     */
    suspend fun send(message: Message): NetworkResult<Unit>

    /**
     * Retrieves the messages that have not been processed yet.
     * The messages are removed from the network state after this method is called.
     * The list returned preserves the received order.
     */
    suspend fun unprocessedMessages(): NetworkResult<List<Message>>

    /**
     * A flow that emits the messages received from the network in asynchronous way.
     */
    fun messagesFlow(): Flow<Message>
}
