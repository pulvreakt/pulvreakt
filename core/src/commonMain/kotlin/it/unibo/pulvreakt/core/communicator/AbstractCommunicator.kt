package it.unibo.pulvreakt.core.communicator

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.core.component.Component
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import mu.KotlinLogging

abstract class AbstractCommunicator : Communicator {
    private var currentMode: Mode = Mode.Local
    private val logger = KotlinLogging.logger("AbstractCommunicator")
    private lateinit var sourceComponent: Component<*>
    private lateinit var destinationComponent: Component<*>

    override suspend fun communicatorSetup(source: Component<*>, destination: Component<*>) {
        sourceComponent = source
        destinationComponent = destination
    }

    final override suspend fun sendToComponent(message: ByteArray): Either<String, Unit> = either {
        when (currentMode) {
            is Mode.Local -> TODO()
            is Mode.Remote -> sendRemoteToComponent(message).bind()
        }
    }

    final override suspend fun receiveFromComponent(): Either<String, Flow<ByteArray>> = either {
        val remoteCommunicator = receiveRemoteFromComponent().bind()
        val localCommunicator: Flow<ByteArray> = emptyFlow() // TODO("Replace with the local communicator")
        merge(remoteCommunicator.map { Mode.Remote to it }, localCommunicator.map { Mode.Local to it })
            .filter { (mode, _) -> mode == currentMode }
            .map { (_, value) -> value }
            .onEach { logger.debug { "Received '$it' - operation mode '${if (currentMode == Mode.Remote) "Remote" else "Local"}'" } }
    }

    abstract suspend fun sendRemoteToComponent(message: ByteArray): Either<String, Unit>

    abstract suspend fun receiveRemoteFromComponent(): Either<String, Flow<ByteArray>>
}
