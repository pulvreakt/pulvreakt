package it.unibo.pulvreakt.runtime.utils

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.right
import it.unibo.pulvreakt.api.communication.protocol.Entity
import it.unibo.pulvreakt.api.communication.protocol.Protocol
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.errors.protocol.ProtocolError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.kodein.di.DI

class TestProtocol : Protocol {

    private val channels = mutableMapOf<Pair<Entity, Entity>, MutableSharedFlow<ByteArray>>()
    override fun setupContext(context: Context<*>) { }

    override suspend fun setupChannel(source: Entity, destination: Entity) {
        channels[source to destination] = MutableSharedFlow()
    }

    override suspend fun writeToChannel(from: Entity, to: Entity, message: ByteArray): Either<ProtocolError, Unit> = either {
        channels[from to to]?.emit(message) ?: raise(ProtocolError.EntityNotRegistered(to))
    }

    override fun readFromChannel(from: Entity, to: Entity): Either<ProtocolError, Flow<ByteArray>> = either {
        channels[from to to]?.asSharedFlow() ?: raise(ProtocolError.EntityNotRegistered(from))
    }

    override suspend fun initialize(): Either<ProtocolError, Unit> = Unit.right()
    override suspend fun finalize(): Either<ProtocolError, Unit> = Unit.right()
}
