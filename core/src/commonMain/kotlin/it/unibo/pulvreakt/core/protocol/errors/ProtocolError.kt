package it.unibo.pulvreakt.core.protocol.errors

import it.unibo.pulvreakt.core.protocol.Entity
import it.unibo.pulvreakt.core.protocol.Protocol

/**
 * Represents all the possible errors that a [Protocol] can raise.
 */
sealed interface ProtocolError {
    /**
     * Error raised when the [Protocol] tries to communicate with an [entity] that is not registered.
     */
    data class EntityNotRegistered(val entity: Entity) : ProtocolError

    /**
     * Wrap the [exception] raised by the [Protocol] in a [ProtocolError].
     */
    data class ProtocolException(val exception: Throwable) : ProtocolError
}
