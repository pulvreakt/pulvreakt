package it.nicolasfarabegoli.pulverization.runtime.spawner

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.runtime.communication.CommManager
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import it.nicolasfarabegoli.pulverization.runtime.context.ExecutionContext
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.RPP
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.TestCommunicator
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.TestReconfigurator
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.DeploymentUnitRuntimeConfiguration
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.pulverizationRuntime
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.ComponentsRefsContainer
import it.nicolasfarabegoli.pulverization.runtime.utils.BehaviourTest
import it.nicolasfarabegoli.pulverization.runtime.utils.HighCpuUsage
import it.nicolasfarabegoli.pulverization.runtime.utils.Host1
import it.nicolasfarabegoli.pulverization.runtime.utils.Host2
import it.nicolasfarabegoli.pulverization.runtime.utils.SensorsContainerTest
import it.nicolasfarabegoli.pulverization.runtime.utils.availableHosts
import it.nicolasfarabegoli.pulverization.runtime.utils.behaviourTestLogic
import it.nicolasfarabegoli.pulverization.runtime.utils.createComponentsRefs
import it.nicolasfarabegoli.pulverization.runtime.utils.sensorsLogicTest
import it.nicolasfarabegoli.pulverization.runtime.utils.systemConfig
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.serializer
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class SpawnerManagerTest : FreeSpec() {
    private val module = module {
        single { CommManager() }
        single<Communicator> { TestCommunicator() }
        single<RemotePlaceProvider> { RPP }
        single<ExecutionContext> {
            object : ExecutionContext {
                override val host: Host = Host2
                override val deviceID: String = "1"
            }
        }
    }

    init {
        "The spawner manager" - {
            PulverizationKoinModule.koinApp = koinApplication { modules(module) }
            val config: DeploymentUnitRuntimeConfiguration<Unit, Unit, Int, Unit, Unit> =
                pulverizationRuntime(systemConfig, "smartphone", availableHosts) {
                    BehaviourTest() withLogic ::behaviourTestLogic startsOn Host2
                    SensorsContainerTest() withLogic ::sensorsLogicTest startsOn Host2

                    withReconfigurator { TestReconfigurator(MutableSharedFlow(), MutableSharedFlow()) }
                    withCommunicator { TestCommunicator() }
                    withRemotePlaceProvider { RPP }

                    reconfigurationRules {
                        onDevice {
                            HighCpuUsage reconfigures { Behaviour movesTo Host1 }
                        }
                    }
                }
            val componentsRef: ComponentsRefsContainer<Unit, Unit, Int, Unit> =
                config.createComponentsRefs(serializer(), serializer(), serializer(), serializer())
            "can spawn the components" {
                val spawner = SpawnerManager(
                    config.runtimeConfiguration.componentsRuntimeConfiguration,
                    componentsRef,
                )
                spawner.activeComponents() shouldBe emptySet()
                spawner.spawn(Behaviour)
                spawner.activeComponents() shouldBe setOf(Behaviour)

                spawner.spawn(State)
                spawner.activeComponents() shouldBe setOf(Behaviour, State)

                spawner.killAll()
            }
            "can kill a component" {
                val spawner = SpawnerManager(
                    config.runtimeConfiguration.componentsRuntimeConfiguration,
                    componentsRef,
                )
                spawner.spawn(Sensors)
                spawner.spawn(Actuators)
                spawner.activeComponents() shouldBe setOf(Sensors, Actuators)

                spawner.kill(Sensors)
                spawner.activeComponents() shouldBe setOf(Actuators)
                spawner.kill(Behaviour) // Non-active components
                spawner.activeComponents() shouldBe setOf(Actuators)
                spawner.kill(Actuators)
                spawner.activeComponents() shouldBe emptySet()
            }
        }
    }
}
