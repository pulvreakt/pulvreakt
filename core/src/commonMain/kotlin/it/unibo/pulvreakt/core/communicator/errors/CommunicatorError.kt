package it.unibo.pulvreakt.core.communicator.errors

import it.unibo.pulvreakt.core.protocol.errors.ProtocolError

sealed interface CommunicatorError {
    object InjectorNotInitialized : CommunicatorError
    object CommunicatorNotInitialized : CommunicatorError
    data class LiftProtocolError(val error: ProtocolError) : CommunicatorError
}
