package it.unibo.pulvreakt.core.communicator

import arrow.core.Either
import arrow.core.right
import it.unibo.pulvreakt.core.component.Component
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class LocalCommunicator(private val sharedFlow: MutableSharedFlow<ByteArray>) : Communicator {
    override suspend fun communicatorSetup(source: Component<*>, destination: Component<*>) = Unit
    override fun setMode(mode: Mode) = Unit
    override suspend fun sendToComponent(message: ByteArray): Either<String, Unit> {
        sharedFlow.emit(message)
        return Unit.right()
    }
    override suspend fun receiveFromComponent(): Either<String, Flow<ByteArray>> = sharedFlow.asSharedFlow().right()
    override suspend fun initialize(): Either<String, Unit> = Unit.right()
    override suspend fun finalize(): Either<String, Unit> = Unit.right()
}
