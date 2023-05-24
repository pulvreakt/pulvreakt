package it.unibo.pulvreakt.core.communicator

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.utils.PulvreaktKoinComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import mu.KotlinLogging
import org.koin.core.component.inject

abstract class AbstractCommunicator : Communicator, PulvreaktKoinComponent() {
    private val localCommManager by inject<LocalCommunicatorManager>()
    private var currentMode: Mode = Mode.Local
    private val logger = KotlinLogging.logger("AbstractCommunicator")
    private lateinit var localCommunicator: Communicator

    override suspend fun communicatorSetup(source: Component<*>, destination: Component<*>) {
        localCommunicator = localCommManager.getLocalCommunicator(source.name, destination.name)
    }

    final override fun setMode(mode: Mode) { currentMode = mode }

    final override suspend fun sendToComponent(message: ByteArray): Either<String, Unit> = either {
        isLocalCommunicatorInitialized().bind()
        when (currentMode) {
            is Mode.Local -> localCommunicator.sendToComponent(message).bind()
            is Mode.Remote -> sendRemoteToComponent(message).bind()
        }
    }

    final override suspend fun receiveFromComponent(): Either<String, Flow<ByteArray>> = either {
        isLocalCommunicatorInitialized().bind()
        val remoteCommunicator = receiveRemoteFromComponent().bind()
        val localCommunicator = localCommunicator.receiveFromComponent().bind()
        merge(remoteCommunicator.map { Mode.Remote to it }, localCommunicator.map { Mode.Local to it })
            .filter { (mode, _) -> mode == currentMode }
            .map { (_, value) -> value }
            .onEach { logger.info { "Received '$it' - operation mode '${if (currentMode == Mode.Remote) "Remote" else "Local"}'" } }
    }

    abstract suspend fun sendRemoteToComponent(message: ByteArray): Either<String, Unit>

    abstract suspend fun receiveRemoteFromComponent(): Either<String, Flow<ByteArray>>

    private fun isLocalCommunicatorInitialized(): Either<String, Unit> = either {
        ensure(::localCommunicator.isInitialized) {
            """
                Local communicator not initialized.
                The `communicatorSetup` method must be invoked before use the communicator.
            """.trimIndent()
        }
    }
}
