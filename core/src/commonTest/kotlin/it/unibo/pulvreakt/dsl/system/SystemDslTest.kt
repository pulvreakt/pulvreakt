package it.unibo.pulvreakt.dsl.system

import arrow.core.nonEmptyListOf
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.core.component.ComponentTypeDelegate
import it.unibo.pulvreakt.dsl.system.errors.SystemDslError
import it.unibo.pulvreakt.dsl.system.model.CapabilityDelegate

val Component1 by ComponentTypeDelegate<Int>()
val Component2 by ComponentTypeDelegate<Int>()

val HighCPU by CapabilityDelegate()
val EmbeddedDevice by CapabilityDelegate()

class SystemDslTest : StringSpec(
    {
        "The system DSL should raise an error if multiple devices have the same name" {
            val result = pulverizedSystem {
                device("device-1") { }
                device("device-1") { }
            }
            val message = result.leftOrNull() ?: error("An error must be raised when two devices have the same name")
            message shouldBe nonEmptyListOf(SystemDslError.DuplicateDeviceName("device-1"))
        }
        "The system DSL should raise an error if an empty configuration is built" {
            val result = pulverizedSystem { }
            val message = result.leftOrNull() ?: error("An error must be raised when the configuration is empty")
            message shouldBe nonEmptyListOf(SystemDslError.EmptyConfiguration)
        }
        "The system DSL should provide a configuration consistent with its usage" {
            val configResult = pulverizedSystem {
                device("device 1") {
                    Component1 deployableOn HighCPU
                    Component2 deployableOn EmbeddedDevice
                }
            }
            val config = configResult.getOrNull() ?: error("No error must be raised")
            with(config) {
                logicalDevices.size shouldBe 1
                val deviceConfig = logicalDevices.firstOrNull { it.deviceName == "device 1" } ?: error("The config for 'device 1' must be available")
                deviceConfig.componentsRequiredCapabilities shouldBe mapOf(
                    Component1 to setOf(HighCPU),
                    Component2 to setOf(EmbeddedDevice),
                )
            }
        }
        "The system DSL should allow assign to a component multiple capabilities" {
            val configResult = pulverizedSystem {
                device("device 1") {
                    Component1 deployableOn setOf(HighCPU, EmbeddedDevice)
                }
            }
            val config = configResult.getOrNull() ?: error("No error must be raised")
            with(config) {
                logicalDevices.size shouldBe 1
                val deviceConfig = logicalDevices.firstOrNull { it.deviceName == "device 1" } ?: error("The config for 'device 1' must be available")
                deviceConfig.componentsRequiredCapabilities shouldBe mapOf(
                    Component1 to setOf(EmbeddedDevice, HighCPU),
                )
            }
        }
        "The system DSL should allow to assign a capability to multiple components" {
            val configResult = pulverizedSystem {
                device("device 1") {
                    Component1 and Component2 deployableOn HighCPU
                }
            }
            val config = configResult.getOrNull() ?: error("No error must be raised")
            with(config) {
                logicalDevices.size shouldBe 1
                val deviceConfig = logicalDevices.firstOrNull { it.deviceName == "device 1" } ?: error("The config for 'device 1' must be available")
                deviceConfig.componentsRequiredCapabilities shouldBe mapOf(
                    Component1 to setOf(HighCPU),
                    Component2 to setOf(HighCPU),
                )
            }
        }
        "The system DSL should allow to assign multiple component to multiple capabilities" {
            val configResult = pulverizedSystem {
                device("device 1") {
                    Component1 and Component2 deployableOn setOf(HighCPU, EmbeddedDevice)
                }
            }
            val config = configResult.getOrNull() ?: error("No error must be raised")
            with(config) {
                logicalDevices.size shouldBe 1
                val deviceConfig = logicalDevices.firstOrNull { it.deviceName == "device 1" } ?: error("The config for 'device 1' must be available")
                deviceConfig.componentsRequiredCapabilities shouldBe mapOf(
                    Component1 to setOf(HighCPU, EmbeddedDevice),
                    Component2 to setOf(HighCPU, EmbeddedDevice),
                )
            }
        }
    },
)
