package it.unibo.pulvreakt.core.dsl

import arrow.core.Either
import arrow.core.nonEmptyListOf
import arrow.core.nonEmptySetOf
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import it.unibo.pulvreakt.api.component.ComponentKind
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.core.dsl.fixture.ActuatorsTest
import it.unibo.pulvreakt.core.dsl.fixture.BehaviourTest
import it.unibo.pulvreakt.core.dsl.fixture.CommTest
import it.unibo.pulvreakt.core.dsl.fixture.SensorsTest
import it.unibo.pulvreakt.core.dsl.fixture.TestComponent1
import it.unibo.pulvreakt.core.dsl.fixture.TestComponent2
import it.unibo.pulvreakt.core.dsl.fixture.TestProtocol
import it.unibo.pulvreakt.core.dsl.fixture.TestReconfigurationEvent1
import it.unibo.pulvreakt.core.dsl.fixture.TestReconfigurationEvent2
import it.unibo.pulvreakt.core.dsl.fixture.embeddedDeviceCapability
import it.unibo.pulvreakt.core.dsl.fixture.serverCapability
import it.unibo.pulvreakt.core.dsl.fixture.serverHost
import it.unibo.pulvreakt.core.dsl.fixture.smartphoneHost
import it.unibo.pulvreakt.core.dsl.fixture.testInfrastructure
import it.unibo.pulvreakt.dsl.errors.ComponentNotRegistered
import it.unibo.pulvreakt.dsl.errors.EmptyDeploymentConfiguration
import it.unibo.pulvreakt.dsl.errors.EmptySystemConfiguration
import it.unibo.pulvreakt.dsl.errors.InvalidReconfiguration
import it.unibo.pulvreakt.dsl.errors.UnknownComponent
import it.unibo.pulvreakt.dsl.errors.UnspecifiedCapabilities
import it.unibo.pulvreakt.dsl.model.DeviceReconfigurationRule
import it.unibo.pulvreakt.dsl.model.NewConfiguration
import it.unibo.pulvreakt.dsl.pulverization

class PulverizationDslTest : StringSpec(
    {
        "The DSL should prevent with an error the creation of an empty system".config(enabled = false) {
            val config = pulverization {}
            config shouldBe Either.Left(nonEmptyListOf(EmptyDeploymentConfiguration, EmptySystemConfiguration))
        }
        "The DSL should raise an error if no capability is specified for a component" {
            val config = pulverization {
                val myDevice by extendedLogicDevice {
                    withComponent<TestComponent1>()
                }
                deployment(testInfrastructure, TestProtocol()) {
                    device(myDevice) {
                        TestComponent1() startsOn smartphoneHost
                    }
                }
            }
            config shouldBe Either.Left(nonEmptyListOf(UnspecifiedCapabilities(ComponentRef.create<TestComponent1>())))
        }
        "The DSL should raise an error if the deployment do not match the system configuration for the same device" {
            val config = pulverization {
                val myDevice by extendedLogicDevice {
                    val component1 = withComponent<TestComponent1>()
                    val component2 = withComponent<TestComponent2>()
                    component1 requires embeddedDeviceCapability
                    component2 requires embeddedDeviceCapability
                    component1 wiredTo component2
                    component2 wiredTo component1
                }
                deployment(testInfrastructure, TestProtocol()) {
                    device(myDevice) { }
                }
            }
            config shouldBe Either.Left(
                nonEmptyListOf(
                    ComponentNotRegistered("myDevice", ComponentRef.create<TestComponent1>()),
                    ComponentNotRegistered("myDevice", ComponentRef.create<TestComponent2>()),
                ),
            )
        }
        "The DSL should raise an error if a reconfiguration rule move a component into an invalid host".config(
            enabled = false,
        ) {
            val config = pulverization {
                val myDevice by extendedLogicDevice {
                    val component1 = withComponent<TestComponent1>()
                    val component2 = withComponent<TestComponent2>()
                    component1 requires embeddedDeviceCapability
                    component2 requires embeddedDeviceCapability
                    component1 wiredTo component2
                    component2 wiredTo component1
                }
                deployment(testInfrastructure, TestProtocol()) {
                    device(myDevice) {
                        TestComponent1() startsOn smartphoneHost
                        TestComponent2() startsOn smartphoneHost
                        reconfigurationRules {
                            onDevice {
                                TestReconfigurationEvent1() reconfigures (ComponentRef.create<TestComponent1>() movesTo serverHost)
                                TestReconfigurationEvent2() reconfigures (ComponentRef.create<TestComponent2>() movesTo serverHost)
                            }
                        }
                    }
                }
            }
            config.isLeft() shouldBe true
            config shouldBe Either.Left(
                nonEmptyListOf(
                    InvalidReconfiguration(ComponentRef.create<TestComponent1>(), serverHost),
                    InvalidReconfiguration(ComponentRef.create<TestComponent2>(), serverHost),
                ),
            )
        }
        "The DSL should produce an error if an unknown component is registered in the deployment" {
            val config = pulverization {
                val myDevice by logicDevice<Int, Unit, Unit, Unit> {
                    val component1 = withBehaviour<BehaviourTest>()
                    component1 requires embeddedDeviceCapability
                }
                deployment(testInfrastructure, TestProtocol()) {
                    device(myDevice) {
                        TestComponent1() startsOn smartphoneHost
                    }
                }
            }
            config.isLeft() shouldBe true
            config shouldBe Either.Left(
                nonEmptyListOf(
                    ComponentNotRegistered("myDevice", ComponentRef.create<BehaviourTest>(ComponentKind.Behavior)),
                ),
            )
        }
        "The DSL should raise an error is an unknown component is moved in a reconfiguration rule".config(
            enabled = false,
        ) {
            val config = pulverization {
                val myDevice by logicDevice<Int, Unit, Unit, Unit> {
                    val component1 = withBehaviour<BehaviourTest>()
                    component1 requires embeddedDeviceCapability
                }
                deployment(testInfrastructure, TestProtocol()) {
                    device(myDevice) {
                        BehaviourTest() startsOn smartphoneHost
                        reconfigurationRules {
                            onDevice {
                                TestReconfigurationEvent1() reconfigures (ComponentRef.create<TestComponent1>() movesTo serverHost)
                            }
                        }
                    }
                }
            }
            config.isLeft() shouldBe true
            config shouldBe Either.Left(
                nonEmptyListOf(
                    UnknownComponent(ComponentRef.create<TestComponent1>()),
                ),
            )
        }
        "The DSL should produce the configuration when properly used".config(enabled = false) {
            val configResult = pulverization {
                val myDevice by extendedLogicDevice {
                    val component1 = withComponent<TestComponent1>()
                    val component2 = withComponent<TestComponent2>()
                    component1 requires nonEmptySetOf(embeddedDeviceCapability, serverCapability)
                    component2 requires embeddedDeviceCapability
                    component1 wiredTo component2
                    component2 wiredTo component1
                }
                deployment(testInfrastructure, TestProtocol()) {
                    device(myDevice) {
                        TestComponent1() startsOn smartphoneHost
                        TestComponent2() startsOn smartphoneHost
                        reconfigurationRules {
                            onDevice {
                                TestReconfigurationEvent1() reconfigures (theComponent<TestComponent1>() movesTo serverHost)
                            }
                        }
                    }
                }
            }
            configResult.isRight() shouldBe true
            configResult.getOrNull()?.let { config ->
                config["my device"] shouldNotBe null
                config["my device"]!!.let { deviceSpec ->
                    deviceSpec.componentsConfiguration shouldBe mapOf(
                        ComponentRef.create<TestComponent1>() to setOf(ComponentRef.create<TestComponent2>()),
                        ComponentRef.create<TestComponent2>() to setOf(ComponentRef.create<TestComponent1>()),
                    )
                    deviceSpec.runtimeConfiguration.componentStartupHost shouldBe mapOf(
                        TestComponent1() to smartphoneHost,
                        TestComponent2() to smartphoneHost,
                    )
                    deviceSpec.runtimeConfiguration.reconfigurationRules shouldNotBe null
                    deviceSpec.runtimeConfiguration.reconfigurationRules!!.let { reconfigRules ->
                        reconfigRules.onDeviceRules shouldBe setOf(
                            DeviceReconfigurationRule(
                                TestReconfigurationEvent1(),
                                NewConfiguration(ComponentRef.create<TestComponent1>(), serverHost),
                            ),
                        )
                    }
                }
            } ?: error("The configuration should be present")
        }
        "The DSL should configure a device with the classical model".config(enabled = false) {
            val configResult = pulverization {
                val myDevice by logicDevice<Int, Unit, Unit, Unit> {
                    val component1 = withBehaviour<BehaviourTest>()
                    val component2 = withCommunication<CommTest>()
                    component1 requires nonEmptySetOf(embeddedDeviceCapability, serverCapability)
                    component2 requires embeddedDeviceCapability
                }
                deployment(testInfrastructure, TestProtocol()) {
                    device(myDevice) {
                        BehaviourTest() startsOn smartphoneHost
                        CommTest() startsOn smartphoneHost
                        reconfigurationRules {
                            onDevice {
                                TestReconfigurationEvent1() reconfigures (theBehaviour<BehaviourTest>() movesTo serverHost)
                            }
                        }
                    }
                }
            }
            configResult.isRight() shouldBe true
            configResult.getOrNull()!!.let { config ->
                config["my device"] shouldNotBe null
                config["my device"]!!.let { deviceSpec ->
                    deviceSpec.componentsConfiguration shouldBe mapOf(
                        ComponentRef.create<BehaviourTest>(ComponentKind.Behavior) to setOf(
                            ComponentRef.create<CommTest>(ComponentKind.Communication),
                        ),
                        ComponentRef.create<CommTest>(ComponentKind.Communication) to setOf(
                            ComponentRef.create<BehaviourTest>(ComponentKind.Behavior),
                        ),
                    )
                    deviceSpec.requiredCapabilities shouldBe mapOf(
                        ComponentRef.create<BehaviourTest>(ComponentKind.Behavior) to nonEmptySetOf(
                            embeddedDeviceCapability,
                            serverCapability,
                        ),
                        ComponentRef.create<CommTest>(ComponentKind.Communication) to nonEmptySetOf(
                            embeddedDeviceCapability,
                        ),
                    )
                }
            }
        }
        "The DSL should admit a mixed configuration with simple device and extended one" {
            val configResult = pulverization {
                val device1 by logicDevice<Int, Unit, Unit, Unit> {
                    val component1 = withBehaviour<BehaviourTest>()
                    val component2 = withCommunication<CommTest>()
                    component1 requires nonEmptySetOf(embeddedDeviceCapability, serverCapability)
                    component2 requires embeddedDeviceCapability
                }
                val device2 by extendedLogicDevice {
                    val component1 = withComponent<TestComponent1>()
                    val component2 = withComponent<TestComponent2>()
                    component1 requires embeddedDeviceCapability
                    component2 requires embeddedDeviceCapability
                    component1 wiredTo component2
                    component2 wiredTo component1
                }
                deployment(testInfrastructure, TestProtocol()) {
                    device(device1) {
                        BehaviourTest() startsOn smartphoneHost
                        CommTest() startsOn smartphoneHost
                    }
                    device(device2) {
                        TestComponent1() startsOn smartphoneHost
                        TestComponent2() startsOn smartphoneHost
                    }
                }
            }
            configResult.isRight() shouldBe true
            configResult.getOrNull()!!.let { config ->
                config["device1"] shouldNotBe null
                config["device2"] shouldNotBe null
            }
        }
        "Regression test: Behaviour, Sensors and Actuators" {
            val configResult = pulverization {
                val device by logicDevice<Int, Unit, Unit, Unit> {
                    withBehaviour<BehaviourTest>() requires serverCapability
                    withSensors<SensorsTest>() requires embeddedDeviceCapability
                    withActuators<ActuatorsTest>() requires embeddedDeviceCapability
                }
                deployment(testInfrastructure, TestProtocol()) {
                    device(device) {
                        BehaviourTest() startsOn serverHost
                        SensorsTest() startsOn smartphoneHost
                        ActuatorsTest() startsOn smartphoneHost
                    }
                }
            }
            configResult.isRight() shouldBe true
            configResult.getOrNull()!!.let { config ->
                config["device"] shouldNotBe null
                val deviceSpec = config["device"]!!
                deviceSpec.componentsConfiguration shouldBe mapOf(
                    ComponentRef.create<BehaviourTest>(ComponentKind.Behavior) to setOf(
                        ComponentRef.create<SensorsTest>(ComponentKind.Sensor),
                        ComponentRef.create<ActuatorsTest>(ComponentKind.Actuator),
                    ),
                    ComponentRef.create<SensorsTest>(ComponentKind.Sensor) to setOf(
                        ComponentRef.create<BehaviourTest>(ComponentKind.Behavior),
                    ),
                    ComponentRef.create<ActuatorsTest>(ComponentKind.Actuator) to setOf(
                        ComponentRef.create<BehaviourTest>(ComponentKind.Behavior),
                    ),
                )
            }
        }
    },
)
