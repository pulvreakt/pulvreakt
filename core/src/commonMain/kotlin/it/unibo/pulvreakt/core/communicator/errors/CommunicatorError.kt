package it.unibo.pulvreakt.core.communicator.errors

import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.protocol.Protocol
import it.unibo.pulvreakt.core.protocol.errors.ProtocolError

/**
 * Represents all the possible errors that a [Communicator] can raise.
 */
sealed interface CommunicatorError {
    /**
     * Error raised because the [Communicator] has the dependency injection module not initialized.
     */
    data object InjectorNotInitialized : CommunicatorError

    /**
     * Error raised because the [Communicator] has not been initialized.
     */
    data object CommunicatorNotInitialized : CommunicatorError

    /**
     * Rethrow the [error] raised by the [Protocol] in the context of the [Communicator].
     * This is used to propagate the underling [ProtocolError] to the [Communicator].
     */
    data class WrapProtocolError(val error: ProtocolError) : CommunicatorError
}
