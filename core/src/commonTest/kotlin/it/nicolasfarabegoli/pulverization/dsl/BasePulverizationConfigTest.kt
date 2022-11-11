package it.nicolasfarabegoli.pulverization.dsl

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class BasePulverizationConfigTest : FunSpec(
    {
        context("The configuration DSL") {
            test("should configure a logical device") {
                val config = pulverizationConfig {
                    logicalDevice("device-1") {
                        component<DSLFixtures.MyState>() deployableOn Cloud
                        component<DSLFixtures.MyBehaviour>() deployableOn Edge
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
            test("An exception should occur when try to configure a logical device with the same name") {
                val exc = shouldThrow<Exception> {
                    pulverizationConfig {
                        logicalDevice("device-1") { }
                        logicalDevice("device-1") { }
                    }
                }
                exc.message shouldContain "device-1"
            }
            test("No exception should be thrown on a right configuration") {
                shouldNotThrow<IllegalStateException> {
                    pulverizationConfig {
                        logicalDevice("device-1") { }
                        logicalDevice("device-2") { }
                    }
                }
            }
            test("An exception should be thrown if the same component appear in multiple deployment units") {
                val exc = shouldThrow<IllegalStateException> {
                    pulverizationConfig {
                        logicalDevice("device-1") {
                            component<DSLFixtures.MyState>() deployableOn Cloud
                            component<DSLFixtures.MyBehaviour>() and component<DSLFixtures.MyState>() deployableOn Edge
                        }
                    }
                }
                exc.message shouldContain "A component appear in more than one deployment unit"
            }
            test("An exception should be thrown when the same component is in the deployment unit") {
                shouldThrow<IllegalStateException> {
                    pulverizationConfig {
                        logicalDevice("device-1") {
                            component<DSLFixtures.MyBehaviour>() and
                                component<DSLFixtures.MyState>() and
                                component<DSLFixtures.MyState>() deployableOn Cloud
                        }
                    }
                }
            }
        }
    },
)
