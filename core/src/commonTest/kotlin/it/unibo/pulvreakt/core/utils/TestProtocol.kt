package it.unibo.pulvreakt.core.utils

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.right
import it.unibo.pulvreakt.core.protocol.Entity
import it.unibo.pulvreakt.core.protocol.Protocol
import it.unibo.pulvreakt.core.protocol.errors.ProtocolError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.kodein.di.DI

class TestProtocol : Protocol {
    override lateinit var di: DI

    override fun setupInjector(kodein: DI) {
        di = kodein
    }

    private val channels = mutableMapOf<Entity, MutableSharedFlow<ByteArray>>()
    override suspend fun setupChannel(source: Entity, destination: Entity) {
        channels[source] = MutableSharedFlow()
    }

    override suspend fun writeToChannel(from: Entity, to: Entity, message: ByteArray): Either<ProtocolError, Unit> = either {
        channels[to]?.emit(message) ?: raise(ProtocolError.EntityNotRegistered(to))
    }

    override fun readFromChannel(from: Entity, to: Entity): Either<ProtocolError, Flow<ByteArray>> = either {
        channels[from]?.asSharedFlow() ?: raise(ProtocolError.EntityNotRegistered(from))
    }

    override suspend fun initialize(): Either<ProtocolError, Unit> = Unit.right()
    override suspend fun finalize(): Either<ProtocolError, Unit> = Unit.right()
}
