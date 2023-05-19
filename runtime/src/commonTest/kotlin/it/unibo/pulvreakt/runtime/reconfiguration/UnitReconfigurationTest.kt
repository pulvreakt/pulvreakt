package it.unibo.pulvreakt.runtime.reconfiguration

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.dsl.model.Behaviour
import it.unibo.pulvreakt.dsl.model.Sensors
import it.unibo.pulvreakt.runtime.communication.CommManager
import it.unibo.pulvreakt.runtime.communication.Communicator
import it.unibo.pulvreakt.runtime.communication.RemotePlaceProvider
import it.unibo.pulvreakt.runtime.componentsref.ComponentRef
import it.unibo.pulvreakt.runtime.context.ExecutionContext
import it.unibo.pulvreakt.runtime.dsl.RemotePlaceProviderTest
import it.unibo.pulvreakt.runtime.dsl.TestCommunicator
import it.unibo.pulvreakt.runtime.dsl.TestReconfigurator
import it.unibo.pulvreakt.runtime.dsl.model.DeploymentUnitRuntimeConfiguration
import it.unibo.pulvreakt.runtime.dsl.model.Host
import it.unibo.pulvreakt.runtime.dsl.model.ReconfigurationSuccess
import it.unibo.pulvreakt.runtime.dsl.model.reconfigurationRules
import it.unibo.pulvreakt.runtime.dsl.pulverizationRuntime
import it.unibo.pulvreakt.runtime.spawner.SpawnerManager
import it.unibo.pulvreakt.runtime.utils.BehaviourTest
import it.unibo.pulvreakt.runtime.utils.HighCpuUsage
import it.unibo.pulvreakt.runtime.utils.Host1
import it.unibo.pulvreakt.runtime.utils.Host2
import it.unibo.pulvreakt.runtime.utils.SensorsContainerTest
import it.unibo.pulvreakt.runtime.utils.availableHosts
import it.unibo.pulvreakt.runtime.utils.behaviourTestLogic
import it.unibo.pulvreakt.runtime.utils.createComponentsRefs
import it.unibo.pulvreakt.runtime.utils.highCpuUsageFlow
import it.unibo.pulvreakt.runtime.utils.sensorsLogicTest
import it.unibo.pulvreakt.runtime.utils.setupOperationMode
import it.unibo.pulvreakt.runtime.utils.setupRefs
import it.unibo.pulvreakt.runtime.utils.systemConfig
import it.unibo.pulvreakt.utils.PulverizationKoinModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class UnitReconfigurationTest : FreeSpec(), KoinTest {
    private val module = module {
        single { CommManager() }
        single<Communicator> { communicator }
        single<RemotePlaceProvider> { RemotePlaceProviderTest }
        single<ExecutionContext> {
            object : ExecutionContext {
                override val host: Host = Host2
                override val deviceID: String = "1"
            }
        }
    }
    private val inFlow = MutableSharedFlow<ByteArray>(1)
    private val outFlow = MutableSharedFlow<ByteArray>(1)
    private val communicator = TestCommunicator(inFlow, outFlow)

    init {
        "The ReconfigurationUnit" - {
            PulverizationKoinModule.koinApp = koinApplication { modules(module) }
            val config: DeploymentUnitRuntimeConfiguration<Unit, Unit, Int, Unit, Unit> =
                pulverizationRuntime(systemConfig, "smartphone", availableHosts) {
                    BehaviourTest() withLogic ::behaviourTestLogic startsOn Host2
                    SensorsContainerTest() withLogic ::sensorsLogicTest startsOn Host2

                    withReconfigurator { TestReconfigurator(MutableSharedFlow(), MutableSharedFlow()) }
                    withCommunicator { communicator }
                    withRemotePlaceProvider { RemotePlaceProviderTest }

                    reconfigurationRules {
                        onDevice {
                            HighCpuUsage reconfigures { Behaviour movesTo Host1 }
                        }
                    }
                }
            val componentsRef: ComponentsRefsContainer<Unit, Unit, Int, Unit> =
                config.createComponentsRefs(serializer(), serializer(), serializer(), serializer())
            val spawner = SpawnerManager(
                config.runtimeConfiguration.componentsRuntimeConfiguration,
                componentsRef,
            )
            val unitReconfigurator = UnitReconfigurator(
                config.runtimeConfiguration.reconfiguratorProvider(),
                config.reconfigurationRules(),
                componentsRef,
                spawner,
                config.startupComponent(Host2),
            )
            "when the condition of an event should trigger a reconfiguration" - {
                "the operation mode should change accordingly".config(enabled = false, timeout = 1.seconds) {
                    componentsRef.setupRefs()
                    componentsRef.setupOperationMode(config.hostComponentsStartupMap(), Host2)
                    unitReconfigurator.initialize()
                    spawner.spawn(Behaviour)
                    spawner.spawn(Sensors)
                    spawner.activeComponents() shouldBe setOf(Behaviour, Sensors)
                    componentsRef.behaviourRefs.sensorsRef.operationMode shouldBe ComponentRef.OperationMode.Local
                    componentsRef.sensorsToBehaviourRef.operationMode shouldBe ComponentRef.OperationMode.Local

                    highCpuUsageFlow.emit(0.95) // Trigger a reconfiguration
                    delay(100) // wait the reconfiguration

                    componentsRef.sensorsToBehaviourRef.operationMode shouldBe ComponentRef.OperationMode.Remote
                    spawner.activeComponents() shouldBe setOf(Sensors)
                    spawner.killAll()
                    unitReconfigurator.finalize()
                }
                "the component ref should receive the messages from the new source".config(timeout = 5.seconds) {
                    componentsRef.setupRefs()
                    componentsRef.setupOperationMode(config.hostComponentsStartupMap(), Host2)
                    unitReconfigurator.initialize()
                    spawner.spawn(Behaviour)
                    spawner.spawn(Sensors)
                    componentsRef.behaviourRefs.sensorsRef.operationMode shouldBe ComponentRef.OperationMode.Local
                    componentsRef.sensorsToBehaviourRef.operationMode shouldBe ComponentRef.OperationMode.Local
                    componentsRef.behaviourRefs.sensorsRef.receiveFromComponent().first() shouldBeInRange (0..100)

                    highCpuUsageFlow.emit(0.95) // Trigger a reconfiguration
                    delay(100)

                    outFlow.map { Json.decodeFromString<Int>(it.decodeToString()) }.take(5).collect {
                        it shouldBeInRange (0..100)
                    }

                    spawner.activeComponents() shouldBe setOf(Sensors)
                    spawner.killAll()
                    unitReconfigurator.finalize()
                }
            }
            "should return a result for each processed result".config(enabled = false, timeout = 1.seconds) {
                componentsRef.setupRefs()
                componentsRef.setupOperationMode(config.hostComponentsStartupMap(), Host2)
                unitReconfigurator.initialize()
                spawner.spawn(Behaviour)
                spawner.spawn(Sensors)
                componentsRef.behaviourRefs.sensorsRef.operationMode shouldBe ComponentRef.OperationMode.Local
                componentsRef.sensorsToBehaviourRef.operationMode shouldBe ComponentRef.OperationMode.Local
                componentsRef.behaviourRefs.sensorsRef.receiveFromComponent().first() shouldBeInRange (0..100)

                highCpuUsageFlow.emit(0.95)
                delay(100.milliseconds)

                HighCpuUsage.results.first() shouldBe ReconfigurationSuccess(0.95)

                spawner.killAll()
                unitReconfigurator.finalize()
            }
        }
    }
}
