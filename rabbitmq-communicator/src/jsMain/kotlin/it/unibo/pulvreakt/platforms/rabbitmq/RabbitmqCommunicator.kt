package it.unibo.pulvreakt.platforms.rabbitmq

import arrow.core.Either
import it.unibo.pulvreakt.core.communicator.AbstractCommunicator
import it.unibo.pulvreakt.core.communicator.Communicator
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
) : AbstractCommunicator() {
    override suspend fun sendRemoteToComponent(message: ByteArray): Either<String, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun receiveRemoteFromComponent(): Either<String, Flow<ByteArray>> {
        TODO("Not yet implemented")
    }

    override suspend fun initialize(): Either<String, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun finalize(): Either<String, Unit> {
        TODO("Not yet implemented")
    }
}
