package it.unibo.pulvreakt.platforms.rabbitmq

import arrow.core.Either
import it.unibo.pulvreakt.api.protocol.Entity
import it.unibo.pulvreakt.api.protocol.Protocol
import it.unibo.pulvreakt.errors.protocol.ProtocolError
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DI

/**
 * Implement the [Communicator] interface relying on RabbitMQ as a platform for communications.
 */
actual class RabbitmqProtocol actual constructor(
    hostname: String,
    port: Int,
    username: String,
    password: String,
    virtualHost: String,
) : Protocol {
    override lateinit var di: DI

    override fun setupInjector(kodein: DI) {
        di = kodein
    }

    override suspend fun setupChannel(source: Entity, destination: Entity) {
        TODO("Not yet implemented")
    }

    override suspend fun writeToChannel(from: Entity, to: Entity, message: ByteArray): Either<ProtocolError, Unit> {
        TODO("Not yet implemented")
    }

    override fun readFromChannel(from: Entity, to: Entity): Either<ProtocolError, Flow<ByteArray>> {
        TODO("Not yet implemented")
    }

    override suspend fun initialize(): Either<ProtocolError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun finalize(): Either<ProtocolError, Unit> {
        TODO("Not yet implemented")
    }
}
