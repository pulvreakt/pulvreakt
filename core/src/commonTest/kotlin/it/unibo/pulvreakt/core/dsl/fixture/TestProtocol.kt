package it.unibo.pulvreakt.core.dsl.fixture

import arrow.core.Either
import it.unibo.pulvreakt.api.communication.protocol.Entity
import it.unibo.pulvreakt.api.communication.protocol.Protocol
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.errors.protocol.ProtocolError
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DI

class TestProtocol : Protocol {
    override fun setupContext(context: Context<*>) {
        TODO("Not yet implemented")
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
