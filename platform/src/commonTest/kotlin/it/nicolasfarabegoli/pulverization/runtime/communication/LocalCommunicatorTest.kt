package it.nicolasfarabegoli.pulverization.runtime.communication

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.time.Duration.Companion.milliseconds

class LocalCommunicatorTest : FreeSpec(), KoinTest {
    private val module = module { single { CommManager() } }

    init {
        "The local communicator" - {
            "could not have a self reference" {
                PulverizationKoinModule.koinApp = koinApplication { modules(module) }
                shouldThrow<IllegalStateException> {
                    LocalCommunicator().apply {
                        setup(BehaviourComponent to BehaviourComponent, null)
                    }
                }
                shouldThrow<IllegalStateException> {
                    LocalCommunicator().apply {
                        setup(StateComponent to StateComponent, null)
                    }
                }
            }
            "when used" - {
                "with behaviour and state" - {
                    "should use the right channels".config(invocationTimeout = 500.milliseconds) {
                        PulverizationKoinModule.koinApp = koinApplication { modules(module) }
                        // In this test we simulate two different "process" spawning two separate coroutines
                        val jb1 = launch {
                            val localComm = LocalCommunicator().apply {
                                setup(BehaviourComponent to StateComponent, null)
                            }
                            localComm.fireMessage("hello".encodeToByteArray())
                        }
                        val jb2 = launch {
                            val stateComm = LocalCommunicator().apply {
                                setup(StateComponent to BehaviourComponent, null)
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
                    PulverizationKoinModule.koinApp = koinApplication { modules(module) }
                    val jb1 = launch {
                        val sensorsComm = LocalCommunicator().apply {
                            setup(SensorsComponent to BehaviourComponent, null)
                        }
                        sensorsComm.fireMessage("message-1".encodeToByteArray())
                        sensorsComm.fireMessage("message-1".encodeToByteArray())
                        sensorsComm.fireMessage("message-3".encodeToByteArray())
                    }
                    val jb2 = launch {
                        val behaviourComm = LocalCommunicator().apply {
                            setup(BehaviourComponent to SensorsComponent, null)
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
    }
}
