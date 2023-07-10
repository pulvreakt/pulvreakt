package it.unibo.pulvreakt.core.dsl.fixture

import arrow.core.Either
import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.communicator.Mode
import it.unibo.pulvreakt.core.communicator.errors.CommunicatorError
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.reconfiguration.ReconfigurationMessage
import it.unibo.pulvreakt.core.reconfiguration.Reconfigurator
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DI

class TestCommunicator : Communicator {
    override suspend fun communicatorSetup(source: ComponentRef, destination: ComponentRef): Either<CommunicatorError, Unit> {
        TODO("Not yet implemented")
    }

    override fun setMode(mode: Mode) {
        TODO("Not yet implemented")
    }

    override suspend fun sendToComponent(message: ByteArray): Either<CommunicatorError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun receiveFromComponent(): Either<CommunicatorError, Flow<ByteArray>> {
        TODO("Not yet implemented")
    }

    override suspend fun initialize(): Either<Nothing, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun finalize(): Either<Nothing, Unit> {
        TODO("Not yet implemented")
    }

    override fun setupInjector(kodein: DI) {
        TODO("Not yet implemented")
    }

    override val di: DI
        get() = TODO("Not yet implemented")
}

class TestReconfigurator : Reconfigurator {
    override suspend fun reconfigure(newConfiguration: ReconfigurationMessage) {
        TODO("Not yet implemented")
    }

    override fun receiveConfiguration(): Flow<ReconfigurationMessage> {
        TODO("Not yet implemented")
    }
}
