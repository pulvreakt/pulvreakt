package it.unibo.pulvreakt.runtime.communication

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.api.communication.Channel
import it.unibo.pulvreakt.api.communication.Mode
import it.unibo.pulvreakt.api.communication.protocol.Entity
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.errors.communication.ChannelError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

/**
 * Predefined [Channel] which handle out-of-the-box the communication between components in the same process.
 */
class ChannelImpl : Channel {
    private var currentMode: Mode = Mode.Local
    private val logger = KotlinLogging.logger("AbstractCommunicator")
    private val remoteProtocol by lazy { context.protocolInstance() }
    private val channelManager by lazy { context.channelManager }
    private lateinit var context: Context<*>
    private lateinit var localCommunicator: Channel
    private lateinit var sourceComponent: ComponentRef
    private lateinit var destinationComponent: ComponentRef

    override fun setupContext(context: Context<*>) {
        this.context = context
    }

    override suspend fun channelSetup(source: ComponentRef, destination: ComponentRef): Either<ChannelError, Unit> = either {
        ensure(::context.isInitialized) { ChannelError.InjectorNotInitialized }
        localCommunicator = channelManager.getLocalCommunicator(source, destination)
        sourceComponent = source
        destinationComponent = destination
        remoteProtocol.setupChannel(source.toEntity(), destination.toEntity())
    }

    override fun setMode(mode: Mode) {
        currentMode = mode
    }

    override suspend fun sendToComponent(message: ByteArray): Either<ChannelError, Unit> = either {
        ensure(::context.isInitialized) { ChannelError.InjectorNotInitialized }
        ensure(::localCommunicator.isInitialized) { ChannelError.CommunicatorNotInitialized }
        logger.debug { "Send ${message.decodeToString()} [Mode: $currentMode]" }
        when (currentMode) {
            is Mode.Local -> localCommunicator.sendToComponent(message).bind()
            is Mode.Remote -> sendRemoteToComponent(message).bind()
        }
    }

    override suspend fun receiveFromComponent(): Either<ChannelError, Flow<ByteArray>> = either {
        ensure(::context.isInitialized) { ChannelError.InjectorNotInitialized }
        ensure(::localCommunicator.isInitialized) { ChannelError.CommunicatorNotInitialized }
        val remoteCommunicator = receiveRemoteFromComponent().bind()
        val localCommunicator = localCommunicator.receiveFromComponent().bind()
        merge(remoteCommunicator.map { Mode.Remote to it }, localCommunicator.map { Mode.Local to it })
            .filter { (mode, _) -> mode == currentMode }
            .map { (_, value) -> value }
            .onEach {
                logger.debug { "Received '${it.decodeToString()}' - operation mode '${if (currentMode == Mode.Remote) "Remote" else "Local"}'" }
            }
    }

    override suspend fun initialize(): Either<Nothing, Unit> = Unit.right()

    override suspend fun finalize(): Either<Nothing, Unit> = Unit.right()

    private suspend fun sendRemoteToComponent(message: ByteArray): Either<ChannelError, Unit> =
        remoteProtocol.writeToChannel(sourceComponent.toEntity(), destinationComponent.toEntity(), message)
            .mapLeft { ChannelError.WrapProtocolError(it) }

    private fun receiveRemoteFromComponent(): Either<ChannelError, Flow<ByteArray>> =
        remoteProtocol.readFromChannel(destinationComponent.toEntity(), sourceComponent.toEntity())
            .mapLeft { ChannelError.WrapProtocolError(it) }

    private fun ComponentRef.toEntity(): Entity = Entity(this.name, context.deviceId.toString())
}
