package it.unibo.pulvreakt.errors.communication

import it.unibo.pulvreakt.api.communication.Channel
import it.unibo.pulvreakt.api.communication.protocol.Protocol
import it.unibo.pulvreakt.errors.protocol.ProtocolError

/**
 * Represents all the possible errors that a [Channel] can raise.
 */
sealed interface ChannelError {
    /**
     * Error raised because the [Channel] has the dependency injection module not initialized.
     */
    data object InjectorNotInitialized : ChannelError

    /**
     * Error raised because the [Channel] has not been initialized.
     */
    data object CommunicatorNotInitialized : ChannelError

    /**
     * Rethrow the [error] raised by the [Protocol] in the context of the [Channel].
     * This is used to propagate the underling [ProtocolError] to the [Channel].
     */
    data class WrapProtocolError(val error: ProtocolError) : ChannelError
}
