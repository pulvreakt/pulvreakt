package it.unibo.pulvreakt.api.communication

import arrow.core.Either
import it.unibo.pulvreakt.api.communication.Mode.Local
import it.unibo.pulvreakt.api.communication.Mode.Remote
import it.unibo.pulvreakt.api.component.Component
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.initializable.InjectAwareResource
import it.unibo.pulvreakt.api.initializable.ManagedResource
import it.unibo.pulvreakt.errors.communication.CommunicatorError
import kotlinx.coroutines.flow.Flow

/**
 * Models the possible operation modes of a [Channel].<br>
 * The [Local] mode represents the communication between components in the same process.
 * The [Remote] mode represents the communication between components remotely.
 */
sealed interface Mode {
    /**
     * Communication in the same process.
     */
    data object Local : Mode

    /**
     * Communication remotely.
     */
    data object Remote : Mode
}

/**
 * Represents the ability of a [Component] to communicate with another one.
 */
interface Channel : ManagedResource<Nothing>, InjectAwareResource {
    /**
     * Sets up the communication between the given [source] and [destination] components.
     */
    suspend fun channelSetup(source: ComponentRef, destination: ComponentRef): Either<CommunicatorError, Unit>

    /**
     * Sets the communication mode of the communicator.
     */
    fun setMode(mode: Mode)

    /**
     * Sends a [message] to the component.
     */
    suspend fun sendToComponent(message: ByteArray): Either<CommunicatorError, Unit>

    /**
     * Receives messages from the component.
     */
    suspend fun receiveFromComponent(): Either<CommunicatorError, Flow<ByteArray>>
}
