package it.nicolasfarabegoli.pulverization.config

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import it.nicolasfarabegoli.pulverization.core.get

class BasePulverizationConfigTest : FunSpec(
    {
        context("The configuration DSL") {
            test("should configure a logical device") {
                val config = pulverizationConfig {
                    logicalDevice("device-1") {
                        DSLFixtures.MyState() deployableOn Cloud
                    }
                }
                config.changeTheName("device-1") { _: DSLFixtures.MyState -> }
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
        }
    },
)
