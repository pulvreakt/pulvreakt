package it.nicolasfarabegoli.pulverization.runtime.communication

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class LocalCommunicatorTest : FreeSpec(
    {
        "The local communicator" - {
            "when used" - {
                "with behaviour and state" - {
                    "should use the right channels".config(invocationTimeout = 500.milliseconds) {
                        val localComm = LocalCommunicator(BehaviourComponent to StateComponent)
                        launch {
                            localComm.fireMessage("hello".encodeToByteArray())
                        }
                        val result = LocalCommunicatorManager.stateInstance.receive()
                        result shouldNotBe null
                        result.decodeToString() shouldBe "hello"
                    }
                }
            }
        }
    },
)
