package it.nicolasfarabegoli.pulverization.runtime.context

import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class CreateContextTest : FreeSpec() {
    init {
        "When creating the context" - {
            "with the configuration file" - {
                "should create the DeviceID with the value specified in the config" {
                    val configPath = javaClass.classLoader.getResource(".pulverization.env").path
                    val context = createContext(configPath)
                    context.deviceID shouldBe "1"
                }
            }
            "an exception should be thrown if no config file nor environment variable are found" {
                val exception = shouldThrowUnit<IllegalStateException> {
                    createContext()
                }
                exception.message shouldBe "Unable to find the device ID"
            }
        }
    }
}
