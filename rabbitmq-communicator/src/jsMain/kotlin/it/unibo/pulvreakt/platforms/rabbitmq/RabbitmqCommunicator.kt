package it.unibo.pulvreakt.platforms.rabbitmq

import arrow.core.Either
import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.communicator.CommunicatorImpl
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
) : CommunicatorImpl() {
    override suspend fun sendRemoteToComponent(message: ByteArray): Either<String, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun receiveRemoteFromComponent(): Either<String, Flow<ByteArray>> {
        TODO("Not yet implemented")
    }

    override suspend fun initialize(): Either<Nothing, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun finalize(): Either<Nothing, Unit> {
        TODO("Not yet implemented")
    }
}
