package it.nicolasfarabegoli.pulverization.dsl.v2

import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.dsl.Cloud
import it.nicolasfarabegoli.pulverization.dsl.Device

class DslTest : FreeSpec({
    "The DSL" - {
        "should produce" - {
            "a consistent configuration" {
                val config = pulverizationSystem {
                    device("device-1") {
                        BehaviourComponent and StateComponent deployableOn Cloud startsOn Cloud
                        CommunicationComponent deployableOn Cloud startsOn Cloud
                        SensorsComponent and ActuatorsComponent deployableOn Device startsOn Device
                    }
                }
                config.devicesConfiguration.size shouldBe 1
                config.devicesConfiguration.firstOrNull { it.deviceName == "device-1" }?.let { device ->
                    device.components.size shouldBe 5
                    device.components shouldBe setOf(
                        BehaviourComponent,
                        StateComponent,
                        CommunicationComponent,
                        ActuatorsComponent,
                        SensorsComponent,
                    )
                    device.allocationMap.componentsStartup.size shouldBe 5
                    device.allocationMap.componentsAllocations.size shouldBe 5

                    device.allocationMap.componentsAllocations shouldBe mapOf(
                        BehaviourComponent to setOf(Cloud),
                        StateComponent to setOf(Cloud),
                        CommunicationComponent to setOf(Cloud),
                        SensorsComponent to setOf(Device),
                        ActuatorsComponent to setOf(Device),
                    )

                    device.allocationMap.componentsAllocations
                        .mapValues { (_, v) -> v.first() } shouldBe device.allocationMap.componentsStartup
                } ?: error("The device-1 should be present in the configuration!")
            }
            "a consistent configuration with multiple tier" {
                val config = pulverizationSystem {
                    device("device-1") {
                        BehaviourComponent deployableOn (Cloud or Device) startsOn Cloud
                    }
                }
                config.devicesConfiguration.firstOrNull { it.deviceName == "device-1" }?.let { device ->
                    device.allocationMap.componentsAllocations shouldBe mapOf(
                        BehaviourComponent to setOf(Device, Cloud),
                    )

                    device.allocationMap.componentsStartup shouldBe mapOf(
                        BehaviourComponent to Cloud,
                    )
                }
            }
        }
    }
    "The configuration produced by the DSL" - {
        val config = pulverizationSystem {
            device("device-1") {
                BehaviourComponent and StateComponent deployableOn Cloud startsOn Cloud
            }
        }
        "can be queried by retrieving a device-specific configuration" {
            val deviceConfig = config.getConfigurationByDeviceOrNull("device-1")

            deviceConfig shouldNotBe null
            deviceConfig?.components shouldBe setOf(BehaviourComponent, StateComponent)
        }
        "if queried with an invalid device should return null or an exception" {
            config.getConfigurationByDeviceOrNull("device-2") shouldBe null
            shouldThrowUnit<IllegalStateException> {
                config.getConfigurationByDevice("device-2")
            }
        }
    }
})
