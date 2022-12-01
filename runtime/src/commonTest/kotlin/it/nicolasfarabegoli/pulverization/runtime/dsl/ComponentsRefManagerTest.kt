package it.nicolasfarabegoli.pulverization.runtime.dsl

import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.dsl.Cloud
import it.nicolasfarabegoli.pulverization.dsl.Edge
import it.nicolasfarabegoli.pulverization.dsl.getDeploymentUnit
import it.nicolasfarabegoli.pulverization.dsl.getDeviceConfiguration
import it.nicolasfarabegoli.pulverization.dsl.pulverizationConfig

class ComponentsRefManagerTest : FreeSpec(
    {
        val config = pulverizationConfig {
            logicalDevice("device-1") {
                BehaviourComponent and
                    StateComponent and
                    CommunicationComponent and
                    SensorsComponent and
                    ActuatorsComponent deployableOn Edge
            }
            logicalDevice("device-2") {
                StateComponent and BehaviourComponent deployableOn Cloud
                CommunicationComponent deployableOn Edge
            }
        }
        "The component ref manager" - {
            "based on the given configuration" - {
                "with all local device" - {
                    "should create all components ref with a local communicator" {
                        val deviceConfig = config.getDeviceConfiguration("device-1")!!
                        val deploymentUnit = deviceConfig.getDeploymentUnit(
                            StateComponent,
                            BehaviourComponent,
                            SensorsComponent,
                            ActuatorsComponent,
                            CommunicationComponent,
                        )!!.deployableComponents
                        val (_, _, _, _) = setupComponentsRef<StatePayload, CommPayload, Int, Int>(
                            deviceConfig.components,
                            deploymentUnit,
                            null,
                        )
                        // TODO(check if communicator is local or not, I don't know how)
                    }
                }
                "with a partial pulverization" - {
                    "should create all remote components with the given communicator" {
                        val deviceConfig = config.getDeviceConfiguration("device-2")!!
                        val deploymentUnit =
                            deviceConfig.getDeploymentUnit(BehaviourComponent, StateComponent)!!.deployableComponents
                        val (_, _, _, _) = setupComponentsRef<StatePayload, CommPayload, Int, Int>(
                            deviceConfig.components,
                            deploymentUnit,
                            RemoteCommunicator(),
                        )
                        // TODO(check if communication ref is remote, I don't know how)
                    }
                }
            }
            "should throw an exception if no communicator is given when needed" {
                shouldThrowUnit<IllegalStateException> {
                    val deviceConfig = config.getDeviceConfiguration("device-2")!!
                    val deploymentUnit =
                        deviceConfig.getDeploymentUnit(BehaviourComponent, StateComponent)!!.deployableComponents
                    val (_, _, _, _) =
                        setupComponentsRef<StatePayload, CommPayload, Int, Int>(
                            deviceConfig.components,
                            deploymentUnit,
                            null,
                        )
                }
            }
        }
    },
)
