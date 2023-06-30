package it.unibo.pulvreakt.core.communicator

import arrow.core.Either
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.utils.Initializable
import it.unibo.pulvreakt.core.utils.PulvreaktInjected
import kotlinx.coroutines.flow.Flow

/**
 * Represents the mode of the communicator.
 */
sealed interface Mode {
    /**
     * Represents the local mode.
     */
    object Local : Mode

    /**
     * Represents the remote mode.
     */
    object Remote : Mode
}

/**
 * Represents the ability of a [Component] to communicate with another one.
 */
interface Communicator : Initializable, PulvreaktInjected {
    /**
     * Sets up the communication between the given [source] and [destination] components.
     */
    suspend fun communicatorSetup(source: ComponentRef<*>, destination: ComponentRef<*>): Either<String, Unit>

    /**
     * Sets the communication mode of the communicator.
     */
    fun setMode(mode: Mode)

    /**
     * Sends a [message] to the component.
     */
    suspend fun sendToComponent(message: ByteArray): Either<String, Unit>

    /**
     * Receives messages from the component.
     */
    suspend fun receiveFromComponent(): Either<String, Flow<ByteArray>>
}
