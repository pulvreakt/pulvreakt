package it.unibo.pulvreakt.platforms.rabbitmq

import it.unibo.pulvreakt.runtime.communication.Binding
import it.unibo.pulvreakt.runtime.communication.Communicator
import it.unibo.pulvreakt.runtime.communication.RemotePlace
import kotlinx.coroutines.flow.Flow

/**
 * Implement the [Communicator] interface relying on RabbitMQ as a platform for communications.
 */
@Suppress("UnusedPrivateMember")
actual class RabbitmqCommunicator actual constructor(
    private val hostname: String,
    private val port: Int,
    private val username: String,
    private val password: String,
    private val virtualHost: String,
) : Communicator {
    override suspend fun setup(binding: Binding, remotePlace: RemotePlace?) {
        TODO("Not yet implemented")
    }

    override suspend fun finalize() {
        TODO("Not yet implemented")
    }

    override suspend fun fireMessage(message: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun receiveMessage(): Flow<ByteArray> {
        TODO("Not yet implemented")
    }
}
