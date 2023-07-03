package it.unibo.pulvreakt.core.protocol

import arrow.core.Either
import it.unibo.pulvreakt.core.protocol.errors.ProtocolError
import it.unibo.pulvreakt.core.utils.Initializable
import kotlinx.coroutines.flow.Flow

data class Entity(val entityName: String, val id: String? = null, val metadata: Map<String, String> = emptyMap())

/**
 * Represents the low-level operations needed to communicate with another entity.
 */
interface Protocol : Initializable<ProtocolError> {
    suspend fun setupChannel(entity: Entity)
    suspend fun writeToChannel(to: Entity, message: ByteArray): Either<ProtocolError, Unit>
    fun readFromChannel(from: Entity): Either<ProtocolError, Flow<ByteArray>>
}
