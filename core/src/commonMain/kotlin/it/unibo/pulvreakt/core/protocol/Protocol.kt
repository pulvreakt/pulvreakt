package it.unibo.pulvreakt.core.protocol

import arrow.core.Either
import it.unibo.pulvreakt.core.protocol.errors.ProtocolError
import it.unibo.pulvreakt.core.utils.Initializable
import kotlinx.coroutines.flow.Flow

/**
 * Represents the entity that the [Protocol] is communicating with.
 * An [Entity] is characterized by a [entityName] and an optional [id].
 * The [id] is used to distinguish between multiple entities with the same [entityName].
 * The [metadata] is used to store additional information about the entity.
 */
data class Entity(val entityName: String, val id: String? = null, val metadata: Map<String, String> = emptyMap())

/**
 * Represents the low-level operations needed to communicate with another entity.
 * The communication is done through a channel that is set up by the [Protocol].
 */
interface Protocol : Initializable<ProtocolError> {
    /**
     * Sets up the communication channel with the given [entity].
     */
    suspend fun setupChannel(entity: Entity)

    /**
     * Writes the given [message] to the channel of the given [to] entity.
     */
    suspend fun writeToChannel(to: Entity, message: ByteArray): Either<ProtocolError, Unit>

    /**
     * Reads from the channel of the given [from] entity.
     */
    fun readFromChannel(from: Entity): Either<ProtocolError, Flow<ByteArray>>
}
