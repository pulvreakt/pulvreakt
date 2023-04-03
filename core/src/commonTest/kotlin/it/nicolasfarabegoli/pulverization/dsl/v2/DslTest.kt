package it.nicolasfarabegoli.pulverization.dsl.v2

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
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
                    device.deploymentMap.startsOn.size shouldBe 5
                    device.deploymentMap.allocationComponents.size shouldBe 5

                    device.deploymentMap.allocationComponents shouldBe mapOf(
                        BehaviourComponent to setOf(Cloud),
                        StateComponent to setOf(Cloud),
                        CommunicationComponent to setOf(Cloud),
                        SensorsComponent to setOf(Device),
                        ActuatorsComponent to setOf(Device),
                    )

                    device.deploymentMap.allocationComponents
                        .mapValues { (_, v) -> v.first() } shouldBe device.deploymentMap.startsOn
                } ?: error("The device-1 should be present in the configuration!")
            }
            "a consistent configuration with multiple tier" {
                val config = pulverizationSystem {
                    device("device-1") {
                        BehaviourComponent deployableOn (Cloud or Device) startsOn Cloud
                    }
                }
                config.devicesConfiguration.firstOrNull { it.deviceName == "device-1" }?.let { device ->
                    device.deploymentMap.allocationComponents shouldBe mapOf(
                        BehaviourComponent to setOf(Device, Cloud),
                    )

                    device.deploymentMap.startsOn shouldBe mapOf(
                        BehaviourComponent to Cloud,
                    )
                }
            }
        }
    }
})
