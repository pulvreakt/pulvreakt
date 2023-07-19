package it.unibo.pulvreakt.core.communicator

import arrow.core.Either
import arrow.core.right
import it.unibo.pulvreakt.core.communicator.errors.CommunicatorError
import it.unibo.pulvreakt.core.component.ComponentRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.kodein.di.DI

internal class LocalCommunicator(
    private val inFlow: MutableSharedFlow<ByteArray>,
    private val outFlow: MutableSharedFlow<ByteArray>,
) : Communicator {
    override lateinit var di: DI
    override suspend fun communicatorSetup(source: ComponentRef, destination: ComponentRef): Either<CommunicatorError, Unit> = Unit.right()
    override fun setMode(mode: Mode) = Unit
    override fun setupInjector(kodein: DI) {
        di = kodein
    }

    override suspend fun sendToComponent(message: ByteArray): Either<CommunicatorError, Unit> = outFlow.emit(message).right()
    override suspend fun receiveFromComponent(): Either<CommunicatorError, Flow<ByteArray>> = inFlow.asSharedFlow().right()
    override suspend fun initialize(): Either<Nothing, Unit> = Unit.right()
    override suspend fun finalize(): Either<Nothing, Unit> = Unit.right()
}
