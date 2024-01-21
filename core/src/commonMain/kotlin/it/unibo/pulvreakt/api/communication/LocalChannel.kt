package it.unibo.pulvreakt.api.communication

import arrow.core.Either
import arrow.core.right
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.errors.communication.ChannelError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class LocalChannel(private val inFlow: MutableSharedFlow<ByteArray>, private val outFlow: MutableSharedFlow<ByteArray>) : Channel {
    override fun setupContext(context: Context<*>) { }
    override suspend fun channelSetup(source: ComponentRef, destination: ComponentRef): Either<ChannelError, Unit> = Unit.right()
    override fun setMode(mode: Mode) = Unit
    override suspend fun sendToComponent(message: ByteArray): Either<ChannelError, Unit> = outFlow.emit(message).right()
    override suspend fun receiveFromComponent(): Either<ChannelError, Flow<ByteArray>> = inFlow.asSharedFlow().right()
    override suspend fun initialize(): Either<Nothing, Unit> = Unit.right()
    override suspend fun finalize(): Either<Nothing, Unit> = Unit.right()
}
