package it.unibo.pulvreakt.api.communication.protocol

import arrow.core.Either
import it.unibo.pulvreakt.api.initializable.InjectAwareResource
import it.unibo.pulvreakt.api.initializable.ManagedResource
import it.unibo.pulvreakt.errors.protocol.ProtocolError
import kotlinx.coroutines.flow.Flow

/**
 * Represents the entity that the [Protocol] is communicating with.
 * An [Entity] is characterized by a [entityName] and an optional [id].
 * The [id] is used to distinguish between multiple entities with the same [entityName].
 * The [metadata] is used to store additional information about the entity.
 */
data class Entity(val entityName: String, val id: String? = null, val metadata: Map<String, String> = emptyMap())

/**
 * Defines the effective protocol used to communicate between two entities remotely.
 * The protocol is responsible for setting up the communication channel between the two [Entity] and for sending and receiving messages.
 *
 * Generally, this interface is implemented in a separate module leveraging protocol-specific libraries.
 * The protocol is configured using the configuration DSL and then will be injected into the runtime to establish the communication.
 * The end-user do not interact directly with this interface.
 */
interface Protocol : ManagedResource<ProtocolError>, InjectAwareResource {
    /**
     * This method configures the protocol to communicate between the given [source] and [destination] entities.
     *
     * Multiple call to this method with the same [source] and [destination] should be idempotent.
     * The runtime call this method to set up the communication between the entities, the end-user should not interact directly with this method.
     */
    suspend fun setupChannel(source: Entity, destination: Entity)

    /**
     * Sends a [message] [from] an [Entity] [to] another [Entity].
     *
     * This method can either succeed or fail with a [ProtocolError].
     * Fails if the [from] and [to] entities are not registered in the protocol via the [setupChannel] method.
     */
    suspend fun writeToChannel(from: Entity, to: Entity, message: ByteArray): Either<ProtocolError, Unit>

    /**
     * Starts listening for messages flowing [from] an [Entity] [to] another [Entity].
     * The returned [Flow] is a hot stream of messages that can be consumed by the runtime.
     *
     * This method can either succeed or fail with a [ProtocolError].
     * Fails if the [from] and [to] entities are not registered in the protocol via the [setupChannel] method.
     */
    fun readFromChannel(from: Entity, to: Entity): Either<ProtocolError, Flow<ByteArray>>
}
