package it.nicolasfarabegoli.pulverization.dsl

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State

class BasePulverizationConfigTest : FreeSpec(
    {
        "The configuration DSL" - {
            "should configure a logical device" {
                val config = pulverizationConfig {
                    logicalDevice("device-1") {
                        State deployableOn Cloud
                        Behaviour deployableOn Edge
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
                            State deployableOn Cloud
                            Behaviour and State deployableOn Edge
                        }
                    }
                }
                exc.message shouldContain "A component appear in more than one deployment unit"
            }
            "An exception should be thrown when the same component is in the deployment unit" {
                shouldThrow<IllegalStateException> {
                    pulverizationConfig {
                        logicalDevice("device-1") {
                            Behaviour and State and State deployableOn Cloud
                        }
                    }
                }
            }
            "The set of deployable unit should be accessed via the configuration" {
                val config = pulverizationConfig {
                    logicalDevice("device-1") {
                        State and Behaviour deployableOn Device
                    }
                }
                config.getDeviceConfiguration("device-1")
                    ?.getDeploymentUnit(Behaviour, State)?.let {
                        it.deployableComponents shouldContain State
                        it.deployableComponents shouldContain Behaviour
                    } ?: error("The deployment unit should be not empty")
            }
        }
    },
)
