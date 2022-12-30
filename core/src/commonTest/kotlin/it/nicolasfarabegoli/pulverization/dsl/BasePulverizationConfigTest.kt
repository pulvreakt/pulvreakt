package it.nicolasfarabegoli.pulverization.dsl

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent

class BasePulverizationConfigTest : FreeSpec(
    {
        "The configuration DSL" - {
            "should configure a logical device" {
                val config = pulverizationConfig {
                    logicalDevice("device-1") {
                        StateComponent deployableOn Cloud
                        BehaviourComponent deployableOn Edge
                    }
                    logicalDevice("device-2") { }
                }
                config.devicesConfig.size shouldBe 2
                config.getDeviceConfiguration("device-1")?.let { logicalDevice ->
                    logicalDevice.deviceName shouldBe "device-1"
                    logicalDevice.components.size shouldBe 2
                    logicalDevice.deploymentUnits.size shouldBe 2
                }
            }
            "An exception should occur when try to configure a logical device with the same name" {
                val exc = shouldThrow<Exception> {
                    pulverizationConfig {
                        logicalDevice("device-1") { }
                        logicalDevice("device-1") { }
                    }
                }
                exc.message shouldContain "device-1"
            }
            "No exception should be thrown on a right configuration" {
                shouldNotThrow<IllegalStateException> {
                    pulverizationConfig {
                        logicalDevice("device-1") { }
                        logicalDevice("device-2") { }
                    }
                }
            }
            "An exception should be thrown if the same component appear in multiple deployment units" {
                val exc = shouldThrow<IllegalStateException> {
                    pulverizationConfig {
                        logicalDevice("device-1") {
                            StateComponent deployableOn Cloud
                            BehaviourComponent and StateComponent deployableOn Edge
                        }
                    }
                }
                exc.message shouldContain "A component appear in more than one deployment unit"
            }
            "An exception should be thrown when the same component is in the deployment unit" {
                shouldThrow<IllegalStateException> {
                    pulverizationConfig {
                        logicalDevice("device-1") {
                            BehaviourComponent and StateComponent and StateComponent deployableOn Cloud
                        }
                    }
                }
            }
            "The set of deployable unit should be accessed via the configuration" {
                val config = pulverizationConfig {
                    logicalDevice("device-1") {
                        StateComponent and BehaviourComponent deployableOn Device
                    }
                }
                config.getDeviceConfiguration("device-1")
                    ?.getDeploymentUnit(BehaviourComponent, StateComponent)?.let {
                        it.deployableComponents shouldContain StateComponent
                        it.deployableComponents shouldContain BehaviourComponent
                    } ?: error("The deployment unit should be not empty")
            }
        }
    },
)
