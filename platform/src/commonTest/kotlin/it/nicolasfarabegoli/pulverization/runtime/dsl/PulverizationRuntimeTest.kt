package it.nicolasfarabegoli.pulverization.runtime.dsl

import io.kotest.assertions.throwables.shouldNotThrowUnit
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.string.shouldContain
import it.nicolasfarabegoli.pulverization.dsl.Cloud
import it.nicolasfarabegoli.pulverization.dsl.Edge
import it.nicolasfarabegoli.pulverization.dsl.getDeviceConfiguration
import it.nicolasfarabegoli.pulverization.dsl.pulverizationConfig
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.dsl.PulverizationPlatformScope.Companion.actuatorsLogic
import it.nicolasfarabegoli.pulverization.runtime.dsl.PulverizationPlatformScope.Companion.behaviourLogic
import it.nicolasfarabegoli.pulverization.runtime.dsl.PulverizationPlatformScope.Companion.communicationLogic
import it.nicolasfarabegoli.pulverization.runtime.dsl.PulverizationPlatformScope.Companion.sensorsLogic
import it.nicolasfarabegoli.pulverization.runtime.dsl.PulverizationPlatformScope.Companion.stateLogic
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.context.stopKoin

class PulverizationRuntimeTest : FreeSpec(
    {
        val config = pulverizationConfig {
            logicalDevice("device-1") {
                Behaviour and State deployableOn Edge
                Communication deployableOn Cloud
            }
        }
        "The kotlin type inference".config(enabled = false) - {
            "should infer the right type using only sensors and actuators" {
                pulverizationPlatform(config.getDeviceConfiguration("device-1")!!) {
                    actuatorsLogic(DeviceActuatorContainer()) { _, _: BehaviourRef<Double> -> }
                    sensorsLogic(DeviceSensorContainer()) { _, _: BehaviourRef<Int> -> }
                }
            }
            "should compile mixing sensors and actuators with other component" {
                pulverizationPlatform(config.getDeviceConfiguration("device-1")!!) {
                    behaviourLogic(BehaviourFixture()) { _, _, _, _, _ -> }
                    communicationLogic(CommunicationFixture()) { _, _ -> }
                    sensorsLogic(DeviceSensorContainer()) { _, _ -> }
                    actuatorsLogic(DeviceActuatorContainer()) { _, _ -> }
                }
            }
        }
        "The platform DSL".config(enabled = false) - {
            "when not respect the configuration" - {
                "were more components are registered, should throw an exception" {
                    val exception = shouldThrowUnit<IllegalStateException> {
                        pulverizationPlatform(config.getDeviceConfiguration("device-1")!!) {
                            withContext { deviceID("1") }
                            behaviourLogic(BehaviourFixture()) { _, _, _, _, _ -> }
                            stateLogic(StateFixture()) { _, _ -> }
                            communicationLogic(CommunicationFixture()) { _, _ -> }
                        }.start()
                    }
                    exception.message shouldContain "The configured components doesn't match the configuration"
                    stopKoin()
                }
                "were less components are registered, should throw an exception" {
                    val exception = shouldThrowUnit<IllegalStateException> {
                        pulverizationPlatform(config.getDeviceConfiguration("device-1")!!) {
                            withContext { deviceID("1") }
                            behaviourLogic(BehaviourFixture()) { _, _, _, _, _ -> }
                        }.start()
                    }
                    exception.message shouldContain "The configured components doesn't match the configuration"
                    stopKoin()
                }
            }
            "when a communicator is required but not given" - {
                "should throw an exception" {
                    val exception = shouldThrowUnit<IllegalStateException> {
                        pulverizationPlatform(config.getDeviceConfiguration("device-1")!!) {
                            withContext { deviceID("1") }
                            behaviourLogic(BehaviourFixture()) { _, _, _, _, _ -> }
                            stateLogic(StateFixture()) { _, _ -> }
                        }.start()
                    }
                    exception.message shouldContain "No communicator given"
                    stopKoin()
                }
            }
            "when configured properly should spawn a coroutine for each defined logic" {
                shouldNotThrowUnit<IllegalStateException> {
                    val jobs = pulverizationPlatform(config.getDeviceConfiguration("device-1")!!) {
                        withContext { deviceID("1") }
                        behaviourLogic(BehaviourFixture()) { _, _, _, _, _ -> }
                        stateLogic(StateFixture()) { _, _ -> }
                        withPlatform { RemoteCommunicator(MutableSharedFlow(1)) }
                    }.start()
                    jobs.forEach { it.cancelAndJoin() }
                    stopKoin()
                }
            }
        }
    },
)
