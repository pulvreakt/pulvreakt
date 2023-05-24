package it.unibo.pulvreakt.core.communicator

import arrow.core.Either
import arrow.core.raise.either
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class LocalCommunicatorManagerTest : FreeSpec({
    coroutineTestScope = true
    "The LocalCommunicatorManager" - {
        "should create a communicator between two components" {
            val messagesResult = mutableListOf<String>()
            val manager = LocalCommunicatorManager()
            val comm1 = manager.getLocalCommunicator("component1", "component2")
            val comm2 = manager.getLocalCommunicator("component2", "component1")
            val receiveResult = async(UnconfinedTestDispatcher()) {
                either {
                    val receiveFlow = comm1.receiveFromComponent().bind()
                    receiveFlow.take(2).collect {
                        messagesResult.add(it.decodeToString())
                    }
                }
            }
            val sendResult = either {
                comm2.sendToComponent("test message".encodeToByteArray()).bind()
                comm2.sendToComponent("test message".encodeToByteArray()).bind()
            }
            sendResult shouldBe Either.Right(Unit)
            receiveResult.await() shouldBe Either.Right(Unit)
            messagesResult shouldBe listOf("test message", "test message")
        }
        "should create independent communicators for non-related components" {
            val resultList = mutableListOf<String>()
            val manager = LocalCommunicatorManager()
            val comm1 = manager.getLocalCommunicator("foo", "bar")
            val comm2 = manager.getLocalCommunicator("bar", "foo")
            val nonRelated = manager.getLocalCommunicator("foo", "foobar")
            val receiveResult = async(UnconfinedTestDispatcher()) {
                either {
                    val flow = comm1.receiveFromComponent().bind()
                    flow.take(1).collect { resultList.add(it.decodeToString()) }
                }
            }
            val sendResult = either {
                comm2.sendToComponent("test message".encodeToByteArray()).bind()
                nonRelated.sendToComponent("non-related message".encodeToByteArray()).bind()
            }
            sendResult shouldBe Either.Right(Unit)
            receiveResult.await() shouldBe Either.Right(Unit)
            resultList shouldBe listOf("test message")
        }
    }
})
