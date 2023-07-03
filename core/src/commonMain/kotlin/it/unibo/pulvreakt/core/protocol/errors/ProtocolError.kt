package it.unibo.pulvreakt.core.protocol.errors

import it.unibo.pulvreakt.core.protocol.Entity

sealed interface ProtocolError {
    data class EntityNotRegistered(val entity: Entity) : ProtocolError
}
