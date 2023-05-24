package it.unibo.pulvreakt.core.communicator

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.right
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.FakeUnitManager
import it.unibo.pulvreakt.core.unit.UnitManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get

class FakeCommunicator(private val remoteFlow: MutableSharedFlow<ByteArray>) : AbstractCommunicator() {
    override suspend fun sendRemoteToComponent(message: ByteArray): Either<String, Unit> =
        remoteFlow.emit(message).right()

    override suspend fun receiveRemoteFromComponent(): Either<String, Flow<ByteArray>> =
        remoteFlow.asSharedFlow().right()

    override suspend fun initialize(): Either<String, Unit> = Unit.right()
    override suspend fun finalize(): Either<String, Unit> = Unit.right()
}

class C1 : AbstractComponent<Int>() {
    override val name: String = "C1"
    override suspend fun execute(): Either<String, Unit> = Unit.right()
}

class C2 : AbstractComponent<Int>() {
    override val name: String = "C2"
    override suspend fun execute(): Either<String, Unit> = Unit.right()
}

@OptIn(ExperimentalCoroutinesApi::class)
class CommunicatorTest : FreeSpec(), KoinTest {

    init {
        startKoin {
            modules(
                module {
                    single { LocalCommunicatorManager() }
                    factory<Communicator> { FakeCommunicator(MutableSharedFlow()) }
                    single<UnitManager> { FakeUnitManager() }
                },
            )
        }
        coroutineTestScope = true
        "The Communicator" - {
            "should raise an error" - {
                "when it is not configured and try to send a message" {
                    val remoteFlow = MutableSharedFlow<ByteArray>()
                    val communicator = FakeCommunicator(remoteFlow)
                    val error = communicator.sendToComponent("test".encodeToByteArray()).leftOrNull()
                        ?: error("An error should be raised when used with no setup")
                    error shouldContain "Local communicator not initialized"
                }
                "when it is not configured and try to receive messages" {
                    val remoteFlow = MutableSharedFlow<ByteArray>()
                    val communicator = FakeCommunicator(remoteFlow)
                    val error = communicator.receiveFromComponent().leftOrNull()
                        ?: error("An error should be raised when used with no setup")
                    error shouldContain "Local communicator not initialized"
                }
            }
            "should work in Local mode" {
                val receivedMessages = mutableListOf<String>()
                val c1 = C1()
                val c2 = C2()
                val manager = get<LocalCommunicatorManager>()
                val localCommunicator = manager.getLocalCommunicator("C1", "C2")
                val communicator = get<Communicator>()
                communicator.communicatorSetup(c1, c2)
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
                receivedMessages shouldBe listOf("test 1", "test 2")
                job.join()
            }
        }
    }
}
