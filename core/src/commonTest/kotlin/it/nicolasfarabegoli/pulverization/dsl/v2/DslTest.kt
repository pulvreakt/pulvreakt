package it.nicolasfarabegoli.pulverization.dsl.v2

import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Capability
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State

// User-defined capabilities
object HighComputationalPower : Capability
object HighMemory : Capability
object EmbeddedDevice : Capability

class DslTest : FreeSpec({
    "The DSL" - {
        "should produce" - {
            "a consistent configuration" {
                val config = pulverizationSystem {
                    device("device-1") {
                        Behaviour and Communication deployableOn HighComputationalPower
                        State deployableOn HighMemory
                        Sensors and Actuators deployableOn EmbeddedDevice
                    }
                }
                config.devicesConfiguration.size shouldBe 1
                config.devicesConfiguration.firstOrNull { it.deviceName == "device-1" }?.let { device ->
                    device.components.size shouldBe 5
                    device.components shouldBe setOf(Behaviour, State, Communication, Actuators, Sensors)

                    device.requiredCapabilities.size shouldBe 5

                    device.requiredCapabilities shouldBe mapOf(
                        Behaviour to setOf(HighComputationalPower),
                        State to setOf(HighMemory),
                        Communication to setOf(HighComputationalPower),
                        Sensors to setOf(EmbeddedDevice),
                        Actuators to setOf(EmbeddedDevice),
                    )
                } ?: error("The device-1 should be present in the configuration!")
            }
            "a consistent configuration with multiple capabilities" {
                val config = pulverizationSystem {
                    device("device-1") {
                        Behaviour and State deployableOn (EmbeddedDevice and HighComputationalPower)
                        Sensors and Actuators deployableOn EmbeddedDevice
                        Communication deployableOn (EmbeddedDevice and HighMemory)
                    }
                }
                config.devicesConfiguration.firstOrNull { it.deviceName == "device-1" }?.let { device ->
                    device.requiredCapabilities shouldBe mapOf(
                        Behaviour to setOf(EmbeddedDevice, HighComputationalPower),
                        State to setOf(EmbeddedDevice, HighComputationalPower),
                        Communication to setOf(HighMemory, EmbeddedDevice),
                        Sensors to setOf(EmbeddedDevice),
                        Actuators to setOf(EmbeddedDevice),
                    )
                }
            }
        }
    }
    "The configuration produced by the DSL" - {
        val config = pulverizationSystem {
            device("device-1") {
                Behaviour and State deployableOn HighComputationalPower
            }
        }
        "can be queried by retrieving a device-specific configuration" {
            val deviceConfig = config.getConfigurationByDeviceOrNull("device-1")

            deviceConfig shouldNotBe null
            deviceConfig?.components shouldBe setOf(Behaviour, State)
        }
        "if queried with an invalid device should return null or an exception" {
            config.getConfigurationByDeviceOrNull("device-2") shouldBe null
            shouldThrowUnit<IllegalStateException> {
                config.getConfigurationByDevice("device-2")
            }
        }
    }
})
