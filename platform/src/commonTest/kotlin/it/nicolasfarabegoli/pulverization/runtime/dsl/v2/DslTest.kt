package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.dsl.v2.pulverizationSystem
import it.nicolasfarabegoli.pulverization.runtime.dsl.BehaviourFixture
import it.nicolasfarabegoli.pulverization.runtime.dsl.CommunicationFixture
import it.nicolasfarabegoli.pulverization.runtime.dsl.DeviceActuatorContainer
import it.nicolasfarabegoli.pulverization.runtime.dsl.DeviceSensorContainer
import it.nicolasfarabegoli.pulverization.runtime.dsl.StateFixture
import it.nicolasfarabegoli.pulverization.runtime.dsl.sensorsLogic
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.toHostCapabilityMapping
import kotlinx.coroutines.flow.emptyFlow

val config = pulverizationSystem {
    device("smartphone") {
        Behaviour and Communication deployableOn HighCpu
        State deployableOn HighMemory
        Actuators and Sensors deployableOn EmbeddedDevice
    }
}

val capabilityMapping = setOf(Host1, Host2).toHostCapabilityMapping()

class DslTest : FreeSpec({
    "The runtime DSL" - {
        "should configure the runtime properly" {
            val runtimeConfig = pulverizationRuntime(config, "smartphone", capabilityMapping) {
                BehaviourFixture() startsOn Host2
                CommunicationFixture() startsOn Host1
                StateFixture() startsOn Host2
                DeviceActuatorContainer() startsOn Host2
                DeviceSensorContainer() withLogic ::sensorsLogic startsOn Host2

                reconfigurationRules {
                    onDevice {
                        CpuUsage reconfigures { Behaviour movesTo Host2 }
                        DeviceNetworkChange reconfigures { Behaviour movesTo Host1 }
                        on(emptyFlow<Int>()) { it > 0 } reconfigures { State movesTo Host2 }
                    }
                }
            }
            with(runtimeConfig) {
                with(deviceSpecification) {
                    deviceName shouldBe "smartphone"
                    components shouldBe setOf(Behaviour, State, Communication, Actuators, Sensors)
                    requiredCapabilities shouldBe mapOf(
                        Behaviour to setOf(HighCpu),
                        Communication to setOf(HighCpu),
                        State to setOf(HighMemory),
                        Actuators to setOf(EmbeddedDevice),
                        Sensors to setOf(EmbeddedDevice),
                    )
                }

                with(runtimeConfiguration.componentsRuntimeConfiguration) {
                    behaviourRuntime shouldNotBe null
                    stateRuntime shouldNotBe null
                    communicationRuntime shouldNotBe null
                    actuatorsRuntime shouldNotBe null
                    sensorsRuntime shouldNotBe null
                }

                runtimeConfiguration.reconfigurationRules?.let {
                    it.deviceReconfigurationRules.size shouldBe 3
                } ?: error("Reconfiguration rules must be defined")
            }
        }
    }
})
