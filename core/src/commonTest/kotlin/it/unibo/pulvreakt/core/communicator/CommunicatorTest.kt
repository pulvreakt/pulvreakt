package it.unibo.pulvreakt.core.communicator

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.right
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.core.communicator.errors.CommunicatorError
import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.errors.ComponentError
import it.unibo.pulvreakt.core.context.Context
import it.unibo.pulvreakt.core.protocol.Entity
import it.unibo.pulvreakt.core.protocol.Protocol
import it.unibo.pulvreakt.core.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.core.utils.TestProtocol
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
}

class C1 : AbstractComponent() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
}

class C2 : AbstractComponent() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
}

@OptIn(ExperimentalCoroutinesApi::class)
class CommunicatorTest : StringSpec(
    {
        coroutineTestScope = true
        val deviceId = 1
        val diModule = DI {
            bind { singleton { LocalCommunicatorManager() } }
            bind<Communicator> { provider { CommunicatorImpl() } }
            bind<ComponentModeReconfigurator> { singleton { FakeComponentModeReconfigurator() } }
            bind<Context> {
                singleton {
                    object : Context {
                        override val deviceId: Int = deviceId
                    }
                }
            }
            bind<Protocol> { singleton { TestProtocol() } }
        }

        fun Component.toEntity(): Entity = Entity(this.getRef().name, deviceId.toString())

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
            val c1 = C1()
            val c2 = C2()
            val manager by diModule.instance<LocalCommunicatorManager>()
            val localCommunicator = manager.getLocalCommunicator("C1", "C2")
            val communicator by diModule.instance<Communicator>()
            communicator.setupInjector(diModule)
            communicator.communicatorSetup(c1.getRef(), c2.getRef()) shouldBe Either.Right(Unit)
            communicator.setMode(Mode.Local)
            val job = launch(UnconfinedTestDispatcher()) {
                val resultCollect = either {
                    val receiveFlow = localCommunicator.receiveFromComponent().bind()
                    receiveFlow.take(2).collect {
                        receivedMessages.add(it.decodeToString())
                    }
                }
                resultCollect shouldBe Either.Right(Unit)
            }
            val resultSend = either {
                communicator.sendToComponent("test 1".encodeToByteArray()).bind()
                communicator.sendToComponent("test 2".encodeToByteArray()).bind()
            }
            resultSend shouldBe Either.Right(Unit)
            job.join()
            receivedMessages shouldBe listOf("test 1", "test 2")
        }
        "The Communicator can send messages to the right communicator depending on the set Mode" {
            val receivedMessage = mutableListOf<String>()
            val manager by diModule.instance<LocalCommunicatorManager>()
            val remoteProtocol by diModule.instance<Protocol>()
            val c1 = C1()
            val c2 = C2()
            val localComm = manager.getLocalCommunicator("C1", "C2")
            val communicator = CommunicatorImpl()
            communicator.setupInjector(diModule)
            communicator.communicatorSetup(c1.getRef(), c2.getRef()) shouldBe Either.Right(Unit)
            communicator.setMode(Mode.Local)
            val localJob = launch(UnconfinedTestDispatcher()) {
                val resultCollect = either {
                    val receiveFlow = localComm.receiveFromComponent().bind()
                    receiveFlow.take(1).collect {
                        receivedMessage.add(it.decodeToString())
                    }
                }
                resultCollect shouldBe Either.Right(Unit)
            }
            val localSendResult = either { communicator.sendToComponent("message 1".encodeToByteArray()).bind() }
            localSendResult shouldBe Either.Right(Unit)
            localJob.join()
            receivedMessage shouldBe listOf("message 1")

            communicator.setMode(Mode.Remote)

            val remoteJob = launch(UnconfinedTestDispatcher()) {
                communicator.receiveFromComponent()
                val channel = remoteProtocol.readFromChannel(c2.toEntity()).getOrNull()
                    ?: error("The channel to the `c2` component must be available!")
                channel.take(1).collect { receivedMessage.add(it.decodeToString()) }
            }
            val remoteResult = either { communicator.sendToComponent("message 2".encodeToByteArray()).bind() }
            remoteResult shouldBe Either.Right(Unit)
            remoteJob.join()
            receivedMessage shouldBe listOf("message 1", "message 2")
        }
        "The Communicator should receive messages according to the operation Mode without re-collect" {
            val receivedMessages = mutableListOf<String>()
            val manager by diModule.instance<LocalCommunicatorManager>()
            val remoteProtocol by diModule.instance<Protocol>()
            val c1 = C1()
            val c2 = C2()
            val localComm = manager.getLocalCommunicator("C1", "C2")
            val communicator = CommunicatorImpl()
            communicator.setupInjector(diModule)
            communicator.communicatorSetup(c1.getRef(), c2.getRef()) shouldBe Either.Right(Unit)
            communicator.setMode(Mode.Local)

            val receiveJob = launch(UnconfinedTestDispatcher()) {
                val resultCollect = either {
                    val receiveFlow = communicator.receiveFromComponent().bind()
                    receiveFlow.take(2).collect {
                        receivedMessages.add(it.decodeToString())
                    }
                }
                resultCollect shouldBe Either.Right(Unit)
            }
            localComm.sendToComponent("local message".encodeToByteArray()) shouldBe Either.Right(Unit)
            communicator.setMode(Mode.Remote)
            localComm.sendToComponent("this should not be received".encodeToByteArray()) shouldBe Either.Right(Unit)
            val result = remoteProtocol.writeToChannel(c2.toEntity(), "remote message".encodeToByteArray())
            result.isRight() shouldBe true
            receiveJob.join()
            receivedMessages shouldBe listOf("local message", "remote message")
        }
    },
)
