package it.nicolasfarabegoli.pulverization.runtime.dsl

import io.kotest.assertions.throwables.shouldNotThrowUnit
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.string.shouldContain
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
                        /*val deploymentUnit =*/ deviceConfig.getDeploymentUnit(
                            StateComponent,
                            BehaviourComponent,
                            SensorsComponent,
                            ActuatorsComponent,
                            CommunicationComponent,
                        )!!.deployableComponents
//                        val (_, _, _, _) = setupComponentsRef<StatePayload, CommPayload, Int, Int>(
//                            deviceConfig.components,
//                            deploymentUnit,
//                            null,
//                        )
                        // TODO(check if communicator is local or not, I don't know how)
                    }
                }
                "with a partial pulverization" - {
                    "should create all remote components with the given communicator" {
                        val deviceConfig = config.getDeviceConfiguration("device-2")!!
//                        val deploymentUnit =
                        deviceConfig.getDeploymentUnit(BehaviourComponent, StateComponent)!!.deployableComponents
//                        val (_, _, _, _) = setupComponentsRef<StatePayload, CommPayload, Int, Int>(
//                            deviceConfig.components,
//                            deploymentUnit,
//                            RemoteCommunicator(),
//                        )
                        // TODO(check if communication ref is remote, I don't know how)
                    }
                }
            }
            "should throw an exception if no communicator is given when needed".config(enabled = false) {
                shouldThrowUnit<IllegalStateException> {
                    val deviceConfig = config.getDeviceConfiguration("device-2")!!
//                    val deploymentUnit =
                    deviceConfig.getDeploymentUnit(BehaviourComponent, StateComponent)!!.deployableComponents
//                    val (_, _, _, _) =
//                        setupComponentsRef<StatePayload, CommPayload, Int, Int>(
//                            deviceConfig.components,
//                            deploymentUnit,
//                            null, // In this test the communicator should be provided
//                        )
                }
            }
            "when creating the behaviour ref" - {
                "should throw an exception if no behaviour is present in the config" {
                    val exception = shouldThrowUnit<IllegalStateException> {
                        setupBehaviourRef<StatePayload>(StateComponent, emptySet(), emptySet(), null)
                    }
                    exception.message shouldContain "The Behaviour must be defined!"
                }
                "it should be created remote" {
                    shouldNotThrowUnit<Exception> {
                        val allComponents = setOf(BehaviourComponent, StateComponent)
                        setupBehaviourRef<StatePayload>(
                            StateComponent,
                            allComponents,
                            setOf(StateComponent),
                            RemoteCommunicator(),
                        )
                        // TODO: check if the communicator is remote...
                    }
                }
                "it should throw an exception if we try to create a behaviour ref linked to a non-existing component" {
                    val exception = shouldThrowUnit<IllegalStateException> {
                        val allComponents = setOf(BehaviourComponent, StateComponent)
                        setupBehaviourRef<CommPayload>(
                            CommunicationComponent,
                            allComponents,
                            setOf(StateComponent),
                            RemoteCommunicator(),
                        )
                    }
                    exception.message shouldContain "this component doesn't appear on the configuration"
                }
                "an exception should be thrown if no communicator is given" {
                    val exception = shouldThrowUnit<IllegalStateException> {
                        val allComponents = setOf(BehaviourComponent, StateComponent)
                        setupBehaviourRef<StatePayload>(
                            StateComponent,
                            allComponents,
                            setOf(StateComponent),
                            null,
                        )
                    }
                    exception.message shouldContain "No communicator given"
                }
            }
        }
    },
)
