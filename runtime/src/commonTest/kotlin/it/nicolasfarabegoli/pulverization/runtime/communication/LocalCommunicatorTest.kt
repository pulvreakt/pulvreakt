package it.nicolasfarabegoli.pulverization.runtime.communication

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class LocalCommunicatorTest : FreeSpec(
    {
        "The local communicator" - {
            "could not have a self reference" {
                shouldThrow<IllegalStateException> {
                    LocalCommunicator().apply {
                        setup(BehaviourComponent to BehaviourComponent)
                    }
                }
                shouldThrow<IllegalStateException> {
                    LocalCommunicator().apply {
                        setup(StateComponent to StateComponent)
                    }
                }
            }
            "when used" - {
                "with behaviour and state" - {
                    "should use the right channels".config(invocationTimeout = 500.milliseconds) {
                        // In this test we simulate two different "process" spawning two separate coroutines
                        val jb1 = launch {
                            val localComm = LocalCommunicator().apply {
                                setup(BehaviourComponent to StateComponent)
                            }
                            localComm.fireMessage("hello".encodeToByteArray())
                        }
                        val jb2 = launch {
                            val stateComm = LocalCommunicator().apply {
                                setup(StateComponent to BehaviourComponent)
                            }
                            stateComm.receiveMessage().first().let {
                                it.decodeToString() shouldBe "hello"
                            }
                        }
                        setOf(jb1, jb2).forEach { it.join() }
                    }
                }
            }
            "when the producer send more messages than the consumer can receive" - {
                "only the last message should be received" {
                    val jb1 = launch {
                        val sensorsComm = LocalCommunicator().apply {
                            setup(SensorsComponent to BehaviourComponent)
                        }
                        sensorsComm.fireMessage("message-1".encodeToByteArray())
                        sensorsComm.fireMessage("message-1".encodeToByteArray())
                        sensorsComm.fireMessage("message-3".encodeToByteArray())
                    }
                    val jb2 = launch {
                        val behaviourComm = LocalCommunicator().apply {
                            setup(BehaviourComponent to SensorsComponent)
                        }
                        delay(100) // Simulate a blocking work
                        behaviourComm.receiveMessage().first().let {
                            it.decodeToString() shouldBe "message-3"
                        }
                    }
                    setOf(jb1, jb2).forEach { it.join() }
                }
            }
        }
    },
)
