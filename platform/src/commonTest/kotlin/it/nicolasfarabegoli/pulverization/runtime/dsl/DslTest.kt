package it.nicolasfarabegoli.pulverization.runtime.dsl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import it.nicolasfarabegoli.pulverization.dsl.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.model.State
import it.nicolasfarabegoli.pulverization.dsl.pulverizationSystem
import kotlinx.coroutines.flow.MutableSharedFlow

val config = pulverizationSystem {
    device("smartphone") {
        Behaviour and Communication deployableOn HighCpu
        State deployableOn HighMemory
        Actuators and Sensors deployableOn EmbeddedDevice
    }
}

class DslTest : FreeSpec(
    {
        "The runtime DSL" - {
            "should configure the runtime properly" {
                val hosts = setOf(Host1, Host2, Host3)
                val runtimeConfig = pulverizationRuntime(config, "smartphone", hosts) {
                    BehaviourFixture() startsOn Host1
                    CommunicationFixture() startsOn Host3
                    StateFixture() startsOn Host1
                    DeviceActuatorContainer() startsOn Host2
                    DeviceSensorContainer() withLogic ::sensorsLogic startsOn Host2

                    reconfigurationRules {
                        onDevice {
                            HighCpuUsage reconfigures { Behaviour movesTo Host3 }
                            DeviceNetworkChange reconfigures { Communication movesTo Host1 }
                            on(memoryUsageFlow) { it > 0.70 } reconfigures { State movesTo Host3 }
                        }
                    }

                    withCommunicator { TestCommunicator() }
                    withReconfigurator { TestReconfigurator(MutableSharedFlow(), MutableSharedFlow()) }
                    withRemotePlaceProvider { RemotePlaceProviderTest }
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
    },
)
