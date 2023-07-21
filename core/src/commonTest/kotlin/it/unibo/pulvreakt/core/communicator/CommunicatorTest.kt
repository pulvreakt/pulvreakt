package it.unibo.pulvreakt.core.communicator

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.right
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.core.communicator.errors.CommunicatorError
import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.errors.ComponentError
import it.unibo.pulvreakt.core.context.Context
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.core.protocol.Protocol
import it.unibo.pulvreakt.core.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.core.utils.TestProtocol
import it.unibo.pulvreakt.dsl.model.Capability
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

class FakeComponentModeReconfigurator : ComponentModeReconfigurator {
    override fun receiveModeUpdates(): Flow<Pair<ComponentRef, Mode>> = emptyFlow()
    override suspend fun setMode(component: ComponentRef, mode: Mode) = Unit
}

class C1 : AbstractComponent() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
}

class C2 : AbstractComponent() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
}

class C3 : AbstractComponent() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
}

class C4 : AbstractComponent() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
}

class C5 : AbstractComponent() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
}

class C6 : AbstractComponent() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
}

@OptIn(ExperimentalCoroutinesApi::class)
class CommunicatorTest : StringSpec(
    {
        coroutineTestScope = true
        val deviceId = 1
        val cap by Capability
        val diModule = DI {
            bind { singleton { LocalCommunicatorManager() } }
            bind<Communicator> { provider { CommunicatorImpl() } }
            bind<ComponentModeReconfigurator> { singleton { FakeComponentModeReconfigurator() } }
            bind<Context> { singleton { Context(deviceId, Host("foo", cap)) } }
            bind<Protocol> { singleton { TestProtocol() } }
        }
        "The Communicator should raise an error when the DI injector is not initialized" {
            val communicator by diModule.instance<Communicator>()
            val result = communicator.sendToComponent("fail to send".encodeToByteArray()).leftOrNull()
                ?: error("The communicator must be initialized with the DI module before its usage")
            result shouldBe CommunicatorError.InjectorNotInitialized
        }
        "The Communicator should raise an error when it is not configured and try to send a message" {
            val communicator = CommunicatorImpl()
            communicator.setupInjector(diModule)
            val error = communicator.sendToComponent("test".encodeToByteArray()).leftOrNull()
                ?: error("An error should be raised when used with no setup")
            error shouldBe CommunicatorError.CommunicatorNotInitialized
        }
        "The Communicator should raise an error when it is not configured and try to receive messages" {
            val communicator = CommunicatorImpl()
            communicator.setupInjector(diModule)
            val error = communicator.receiveFromComponent().leftOrNull()
                ?: error("An error should be raised when used with no setup")
            error shouldBe CommunicatorError.CommunicatorNotInitialized
        }
        "The Communicator should work in Local mode" {
            val receivedMessages = mutableListOf<String>()
            val c1Ref = C1().getRef()
            val c2Ref = C2().getRef()
            val c1Communicator by diModule.instance<Communicator>()
            val c2Communicator by diModule.instance<Communicator>()

            with(c1Communicator) {
                setupInjector(diModule)
                communicatorSetup(c1Ref, c2Ref) shouldBe Either.Right(Unit)
                setMode(Mode.Local)
            }

            with(c2Communicator) {
                setupInjector(diModule)
                communicatorSetup(c2Ref, c1Ref) shouldBe Either.Right(Unit)
                setMode(Mode.Local)
            }

            val c2ReceiveJob = launch(UnconfinedTestDispatcher()) {
                val resultCollect = either {
                    val receiveFlow = c2Communicator.receiveFromComponent().bind()
                    receiveFlow.take(2).collect {
                        receivedMessages.add(it.decodeToString())
                    }
                }
                resultCollect shouldBe Either.Right(Unit)
            }

            val resultSend = either {
                c1Communicator.sendToComponent("test 1".encodeToByteArray()).bind()
                c1Communicator.sendToComponent("test 2".encodeToByteArray()).bind()
            }
            resultSend shouldBe Either.Right(Unit)
            c2ReceiveJob.join()
            receivedMessages shouldBe listOf("test 1", "test 2")
        }
        "The Communicator can send messages to the right communicator depending on the set Mode" {
            val receivedMessage = mutableListOf<String>()
            val c3Ref = C3().getRef()
            val c4Ref = C4().getRef()
            val c1Communicator by diModule.instance<Communicator>()
            val c2Communicator by diModule.instance<Communicator>()

            with(c1Communicator) {
                setupInjector(diModule)
                communicatorSetup(c3Ref, c4Ref) shouldBe Either.Right(Unit)
                setMode(Mode.Local)
            }

            with(c2Communicator) {
                setupInjector(diModule)
                communicatorSetup(c4Ref, c3Ref) shouldBe Either.Right(Unit)
                setMode(Mode.Local)
            }

            val localReceiveJob = launch(UnconfinedTestDispatcher()) {
                val resultCollect = either {
                    val receiveFlow = c2Communicator.receiveFromComponent().bind()
                    receiveFlow.take(1).collect {
                        receivedMessage.add(it.decodeToString())
                    }
                }
                resultCollect shouldBe Either.Right(Unit)
            }

            val localSendResult = either { c1Communicator.sendToComponent("message 1".encodeToByteArray()).bind() }
            localSendResult shouldBe Either.Right(Unit)
            localReceiveJob.join()

            receivedMessage shouldBe listOf("message 1")

            // Start Remote communication
            c1Communicator.setMode(Mode.Remote)
            c2Communicator.setMode(Mode.Remote)

            val remoteReceiveJob = launch(UnconfinedTestDispatcher()) {
                val resultCollect = either {
                    val receiveFlow = c2Communicator.receiveFromComponent().bind()
                    receiveFlow.take(1).collect {
                        receivedMessage.add(it.decodeToString())
                    }
                }
                resultCollect shouldBe Either.Right(Unit)
            }

            val remoteSendResult = either { c1Communicator.sendToComponent("message 2".encodeToByteArray()).bind() }
            remoteSendResult shouldBe Either.Right(Unit)
            remoteReceiveJob.join()

            receivedMessage shouldBe listOf("message 1", "message 2")
        }
        "The Communicator should receive messages according to the operation Mode without re-collect" {
            val receivedMessages = mutableListOf<String>()
            val c5Ref = C5().getRef()
            val c6Ref = C6().getRef()

            val c5Communicator by diModule.instance<Communicator>()
            val c6Communicator by diModule.instance<Communicator>()

            with(c5Communicator) {
                setupInjector(diModule)
                communicatorSetup(c5Ref, c6Ref) shouldBe Either.Right(Unit)
                setMode(Mode.Local)
            }

            with(c6Communicator) {
                setupInjector(diModule)
                communicatorSetup(c6Ref, c5Ref) shouldBe Either.Right(Unit)
                setMode(Mode.Local)
            }

            val receiveJob = launch(UnconfinedTestDispatcher()) {
                val resultCollect = either {
                    val receiveFlow = c6Communicator.receiveFromComponent().bind()
                    receiveFlow.take(2).collect {
                        receivedMessages.add(it.decodeToString())
                    }
                }
                resultCollect shouldBe Either.Right(Unit)
            }

            val sendResult = either {
                c5Communicator.sendToComponent("test 1".encodeToByteArray()).bind()
                // Switch to Remote mode
                c5Communicator.setMode(Mode.Remote)
                c6Communicator.setMode(Mode.Remote)
                c5Communicator.sendToComponent("test 2".encodeToByteArray()).bind()
            }
            sendResult shouldBe Either.Right(Unit)
            receiveJob.join()
            receivedMessages shouldBe listOf("test 1", "test 2")
        }
    },
)
