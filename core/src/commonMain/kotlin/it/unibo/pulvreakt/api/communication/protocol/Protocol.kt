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
     * Sets up the communication channel with the given [source].
     */
    suspend fun setupChannel(source: Entity, destination: Entity)

    /**
     * Writes the given [message] to the channel of the given [to] entity.
     */
    suspend fun writeToChannel(from: Entity, to: Entity, message: ByteArray): Either<ProtocolError, Unit>

    /**
     * Reads from the channel of the given [from] entity.
     */
    fun readFromChannel(from: Entity, to: Entity): Either<ProtocolError, Flow<ByteArray>>
}
