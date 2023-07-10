package it.unibo.pulvreakt.core.communicator

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.core.communicator.errors.CommunicatorError
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.context.Context
import it.unibo.pulvreakt.core.protocol.Entity
import it.unibo.pulvreakt.core.protocol.Protocol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import org.kodein.di.DI
import org.kodein.di.instance

/**
 * Predefined [Communicator] which handle out-of-the-box the communication between components in the same process.
 */
class CommunicatorImpl : Communicator {
    override lateinit var di: DI
    private val localCommManager by instance<LocalCommunicatorManager>()
    private var currentMode: Mode = Mode.Local
    private val logger = KotlinLogging.logger("AbstractCommunicator")
    private lateinit var localCommunicator: Communicator
    private val remoteProtocol by instance<Protocol>()
    private val context by instance<Context>()
    private lateinit var sourceComponent: ComponentRef
    private lateinit var destinationComponent: ComponentRef

    override suspend fun communicatorSetup(source: ComponentRef, destination: ComponentRef): Either<CommunicatorError, Unit> = either {
        isDependencyInjectionInitialized().bind()
        localCommunicator = localCommManager.getLocalCommunicator(source.name, destination.name)
        sourceComponent = source
        destinationComponent = destination
        remoteProtocol.setupChannel(destination.toEntity())
    }

    override fun setupInjector(kodein: DI) {
        di = kodein
    }

    override fun setMode(mode: Mode) {
        currentMode = mode
    }

    override suspend fun sendToComponent(message: ByteArray): Either<CommunicatorError, Unit> = either {
        isDependencyInjectionInitialized().bind()
        isLocalCommunicatorInitialized().bind()
        when (currentMode) {
            is Mode.Local -> localCommunicator.sendToComponent(message).bind()
            is Mode.Remote -> sendRemoteToComponent(message).bind()
        }
    }

    override suspend fun receiveFromComponent(): Either<CommunicatorError, Flow<ByteArray>> = either {
        isDependencyInjectionInitialized().bind()
        isLocalCommunicatorInitialized().bind()
        val remoteCommunicator = receiveRemoteFromComponent().bind()
        val localCommunicator = localCommunicator.receiveFromComponent().bind()
        merge(remoteCommunicator.map { Mode.Remote to it }, localCommunicator.map { Mode.Local to it })
            .filter { (mode, _) -> mode == currentMode }
            .map { (_, value) -> value }
            .onEach {
                logger.debug {
                    "Received '${it.decodeToString()}' - operation mode '${if (currentMode == Mode.Remote) "Remote" else "Local"}'"
                }
            }
    }

    override suspend fun initialize(): Either<Nothing, Unit> = Unit.right()

    override suspend fun finalize(): Either<Nothing, Unit> = Unit.right()

    private suspend fun sendRemoteToComponent(message: ByteArray): Either<CommunicatorError, Unit> =
        remoteProtocol.writeToChannel(destinationComponent.toEntity(), message)
            .mapLeft { CommunicatorError.WrapProtocolError(it) }

    private fun receiveRemoteFromComponent(): Either<CommunicatorError, Flow<ByteArray>> =
        remoteProtocol.readFromChannel(destinationComponent.toEntity())
            .mapLeft { CommunicatorError.WrapProtocolError(it) }

    private fun isDependencyInjectionInitialized(): Either<CommunicatorError, Unit> = either {
        ensure(::di.isInitialized) { CommunicatorError.InjectorNotInitialized }
    }

    private fun isLocalCommunicatorInitialized(): Either<CommunicatorError, Unit> = either {
        ensure(::localCommunicator.isInitialized) { CommunicatorError.CommunicatorNotInitialized }
    }

    private fun ComponentRef.toEntity(): Entity = Entity(this.name, context.deviceId.toString())
}
