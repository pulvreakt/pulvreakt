package it.nicolasfarabegoli.pulverization.runtime.dsl

import io.kotest.assertions.throwables.shouldNotThrowUnit
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.string.shouldContain
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.dsl.Cloud
import it.nicolasfarabegoli.pulverization.dsl.Edge
import it.nicolasfarabegoli.pulverization.dsl.getDeviceConfiguration
import it.nicolasfarabegoli.pulverization.dsl.pulverizationConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.PulverizationPlatformScope.Companion.behaviourLogic
import it.nicolasfarabegoli.pulverization.runtime.dsl.PulverizationPlatformScope.Companion.communicationLogic
import it.nicolasfarabegoli.pulverization.runtime.dsl.PulverizationPlatformScope.Companion.stateLogic
import kotlinx.coroutines.cancelAndJoin

class PulverizationRuntimeTest : FreeSpec(
    {
        val config = pulverizationConfig {
            logicalDevice("device-1") {
                BehaviourComponent and StateComponent deployableOn Edge
                CommunicationComponent deployableOn Cloud
            }
        }
        "The platform DSL" - {
            "when not respect the configuration" - {
                "should throw an exception" {
                    val exception = shouldThrowUnit<IllegalStateException> {
                        pulverizationPlatform(config.getDeviceConfiguration("device-1")!!) {
                            behaviourLogic(FixtureBehaviour()) { _, _, _, _, _ -> }
                            stateLogic(StateFixture()) { _, _ -> }
                            communicationLogic(CommunicationFixture()) { _, _ -> }
                        }.start()
                    }
                    exception.message shouldContain "The configured components doesn't match the configuration"
                }
            }
            "when a communicator is required but not given" - {
                "should throw an exception" {
                    val exception = shouldThrowUnit<IllegalStateException> {
                        pulverizationPlatform(config.getDeviceConfiguration("device-1")!!) {
                            behaviourLogic(FixtureBehaviour()) { _, _, _, _, _ -> }
                            stateLogic(StateFixture()) { _, _ -> }
                        }.start()
                    }
                    exception.message shouldContain "No communicator given"
                }
            }
            "when configured properly should spawn a coroutine for each defined logic" {
                shouldNotThrowUnit<IllegalStateException> {
                    val jobs = pulverizationPlatform(config.getDeviceConfiguration("device-1")!!) {
                        behaviourLogic(FixtureBehaviour()) { _, _, _, _, _ -> }
                        stateLogic(StateFixture()) { _, _ -> }
                        withPlatform { RemoteCommunicator() }
                    }.start()
                    jobs.forEach { it.cancelAndJoin() }
                }
            }
        }
    },
)
