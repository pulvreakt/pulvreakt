package it.unibo.pulvreakt.dsl.system

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.dsl.system.model.Capability

object Component1 : ComponentType
object Component2 : ComponentType

object HighCPU : Capability
object EmbeddedDevice : Capability

class SystemDslTest : StringSpec({
    "The system DSL should raise an error if multiple devices have the same name" {
        val result = pulverizedSystem {
            device("device-1") { }
            device("device-1") { }
        }
        val message = result.leftOrNull() ?: error("An error must be raised when two devices have the same name")
        message shouldContain "Multiple device with the same name are not allowed"
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
})