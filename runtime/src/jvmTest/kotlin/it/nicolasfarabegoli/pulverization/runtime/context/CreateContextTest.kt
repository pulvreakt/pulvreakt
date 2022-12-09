package it.nicolasfarabegoli.pulverization.runtime.context

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
            "with the environment variable" - {
                "should create the DeviceID with the value specified in the environment variable" {
                    System.setProperty("DEVICE_ID", "32")
                    val context = createContext()
                    context.deviceID shouldBe "32"
                }
            }
            "and the config file and the environment variable are specified at the same time" - {
                "the config file should be read" {
                    val configPath = javaClass.classLoader.getResource(".pulverization.env").path
                    System.setProperty("DEVICE_ID", "2")
                    val context = createContext(configPath)
                    context.deviceID shouldBe "1"
                }
            }
        }
    }
}
