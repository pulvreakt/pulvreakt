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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

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

object CpuUsage : EventProducer<Double> {
    override val events: Flow<Double> = emptyFlow()
}

object DeviceNetworkChange : EventProducer<Int> {
    override val events: Flow<Int> = emptyFlow()
}

class DslTest : FreeSpec({
    "The runtime DSL" - {
        val config = pulverizationSystem {
            device("smartphone") {
                Behaviour and Communication deployableOn HighCpu
                State deployableOn HighMemory
                Actuators and Sensors deployableOn EmbeddedDevice
            }
        }
        "should configure the runtime properly" {
            pulverizationRuntime(config, "smartphone") {
                FixtureBehaviour() startsOn Host2
                CommunicationFixture() startsOn Host1
                DeviceActuatorContainer() startsOn Host2
                DeviceSensorContainer() withLogic ::sensorsLogic startsOn Host2

                reconfigurationRules {
                    onDevice {
                        condition(CpuUsage) { it > 0.75 } reconfigures { Behaviour movesTo Host2 }
                        condition(DeviceNetworkChange) { it > 5 } reconfigures { Behaviour movesTo Host1 }
                    }
                }
            }
        }
    }
})
