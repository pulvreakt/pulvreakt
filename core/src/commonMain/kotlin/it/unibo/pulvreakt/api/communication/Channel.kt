package it.unibo.pulvreakt.api.communication

import arrow.core.Either
import it.unibo.pulvreakt.api.communication.Mode.Local
import it.unibo.pulvreakt.api.communication.Mode.Remote
import it.unibo.pulvreakt.api.component.Component
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.api.initializable.ManagedResource
import it.unibo.pulvreakt.errors.communication.ChannelError
import kotlinx.coroutines.flow.Flow

/**
 * Models the possible operation modes of a [Channel].
 *
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
 * Models a communication channel between two [Component]s.
 *
 * With the channel the components can communicate with each other via the [sendToComponent] and [receiveFromComponent] methods.
 * The channel is set up by the [channelSetup] method and can change its operation mode with the [setMode] method.
 * The communication [Mode] defines if the communication should occur locally or remotely.
 */
interface Channel : ManagedResource<Nothing> {
    /**
     * Sets up the [Context] of the channel.
     */
    fun setupContext(context: Context<*>)

    /**
     * Sets up the communication between the given [source] and [destination] components.
     */
    suspend fun channelSetup(source: ComponentRef, destination: ComponentRef): Either<ChannelError, Unit>

    /**
     * Sets the communication mode of the communicator.
     */
    fun setMode(mode: Mode)

    /**
     * Sends a [message] to the component.
     */
    suspend fun sendToComponent(message: ByteArray): Either<ChannelError, Unit>

    /**
     * Receives messages from the component.
     */
    suspend fun receiveFromComponent(): Either<ChannelError, Flow<ByteArray>>
}
