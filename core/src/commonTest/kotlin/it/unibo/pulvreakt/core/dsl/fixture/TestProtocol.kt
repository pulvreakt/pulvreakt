package it.unibo.pulvreakt.core.dsl.fixture

import arrow.core.Either
import it.unibo.pulvreakt.core.protocol.Entity
import it.unibo.pulvreakt.core.protocol.Protocol
import it.unibo.pulvreakt.core.protocol.errors.ProtocolError
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DI

class TestProtocol : Protocol {
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