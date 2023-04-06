package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import io.kotest.core.spec.style.FreeSpec
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Capability
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.dsl.v2.pulverizationSystem
import it.nicolasfarabegoli.pulverization.runtime.dsl.CommunicationFixture
import it.nicolasfarabegoli.pulverization.runtime.dsl.DeviceActuatorContainer
import it.nicolasfarabegoli.pulverization.runtime.dsl.DeviceSensorContainer
import it.nicolasfarabegoli.pulverization.runtime.dsl.FixtureBehaviour
import it.nicolasfarabegoli.pulverization.runtime.dsl.sensorsLogic
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host

object HighCpu : Capability
object HighMemory : Capability
object EmbeddedDevice : Capability

object Host1 : Host {
    override val hostname: String = "host1"
    override val capabilities: Set<Capability> = setOf(HighCpu, HighMemory)
}

object Host2 : Host {
    override val hostname: String = "host2"
    override val capabilities: Set<Capability> = setOf(EmbeddedDevice)
}

class DslTest : FreeSpec({
    "The runtime DSL" - {
        val config = pulverizationSystem {
            device("device-1") {
                Behaviour and Communication deployableOn HighCpu
                State deployableOn HighMemory
                Actuators and Sensors deployableOn EmbeddedDevice
            }
        }
        "should configure the runtime properly" {
            pulverizationRuntime(config, "device-1") {
                FixtureBehaviour() runsOn (Host1 or Host2) startOn Host2
                CommunicationFixture() runsOn Host1
                DeviceActuatorContainer() runsOn Host2
                DeviceSensorContainer() withLogic ::sensorsLogic startOn Host2
            }
        }
    }
})
