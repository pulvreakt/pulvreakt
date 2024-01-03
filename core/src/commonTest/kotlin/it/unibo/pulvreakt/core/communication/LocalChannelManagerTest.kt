package it.unibo.pulvreakt.core.communication

import arrow.core.Either
import arrow.core.raise.either
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.api.communication.LocalChannelManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class LocalChannelManagerTest : StringSpec(
    {
        coroutineTestScope = true
        "The LocalCommunicatorManager should create a communicator between two components" {
            val messagesResult = mutableListOf<String>()
            val c1Ref = C1().getRef()
            val c2Ref = C2().getRef()
            val manager = LocalChannelManager()
            val comm1 = manager.getLocalCommunicator(c1Ref, c2Ref)
            val comm2 = manager.getLocalCommunicator(c2Ref, c1Ref)
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
        "The LocalCommunicatorManager should create independent communicators for non-related components" {
            val resultList = mutableListOf<String>()
            val manager = LocalChannelManager()
            val fooRef = C1().getRef()
            val barRef = C2().getRef()
            val foobarRef = C3().getRef()
            val comm1 = manager.getLocalCommunicator(fooRef, barRef)
            val comm2 = manager.getLocalCommunicator(barRef, fooRef)
            val nonRelated = manager.getLocalCommunicator(fooRef, foobarRef)
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
    },
)
