package it.unibo.pulvreakt.runtime.spawner

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.dsl.model.Actuators
import it.unibo.pulvreakt.dsl.model.Behaviour
import it.unibo.pulvreakt.dsl.model.Communication
import it.unibo.pulvreakt.dsl.model.Sensors
import it.unibo.pulvreakt.dsl.model.State
import it.unibo.pulvreakt.runtime.communication.CommManager
import it.unibo.pulvreakt.runtime.communication.Communicator
import it.unibo.pulvreakt.runtime.communication.RemotePlaceProvider
import it.unibo.pulvreakt.runtime.context.ExecutionContext
import it.unibo.pulvreakt.runtime.dsl.RemotePlaceProviderTest
import it.unibo.pulvreakt.runtime.dsl.TestCommunicator
import it.unibo.pulvreakt.runtime.dsl.TestReconfigurator
import it.unibo.pulvreakt.runtime.dsl.model.DeploymentUnitRuntimeConfiguration
import it.unibo.pulvreakt.runtime.dsl.model.Host
import it.unibo.pulvreakt.runtime.dsl.pulverizationRuntime
import it.unibo.pulvreakt.runtime.reconfiguration.ComponentsRefsContainer
import it.unibo.pulvreakt.runtime.utils.BehaviourTest
import it.unibo.pulvreakt.runtime.utils.HighCpuUsage
import it.unibo.pulvreakt.runtime.utils.Host1
import it.unibo.pulvreakt.runtime.utils.Host2
import it.unibo.pulvreakt.runtime.utils.SensorsContainerTest
import it.unibo.pulvreakt.runtime.utils.availableHosts
import it.unibo.pulvreakt.runtime.utils.behaviourTestLogic
import it.unibo.pulvreakt.runtime.utils.createComponentsRefs
import it.unibo.pulvreakt.runtime.utils.sensorsLogicTest
import it.unibo.pulvreakt.runtime.utils.setupRefs
import it.unibo.pulvreakt.runtime.utils.systemConfig
import it.unibo.pulvreakt.utils.PulverizationKoinModule
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.serializer
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class SpawnerManagerTest : FreeSpec() {
    private val module = module {
        single { CommManager() }
        single<Communicator> { TestCommunicator() }
        single<RemotePlaceProvider> { RemotePlaceProviderTest }
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
                    withRemotePlaceProvider { RemotePlaceProviderTest }

                    reconfigurationRules {
                        onDevice {
                            HighCpuUsage reconfigures { Behaviour movesTo Host1 }
                        }
                    }
                }
            val componentsRef: ComponentsRefsContainer<Unit, Unit, Int, Unit> =
                config.createComponentsRefs(serializer(), serializer(), serializer(), serializer())
            componentsRef.setupRefs()
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

                spawner.spawn(Communication)
                spawner.activeComponents() shouldBe setOf(Behaviour, State, Communication)

                spawner.killAll()
                spawner.activeComponents() shouldBe emptySet()
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
