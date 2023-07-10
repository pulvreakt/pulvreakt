package it.unibo.pulvreakt.core.dsl

import arrow.core.Either
import arrow.core.nonEmptyListOf
import arrow.core.nonEmptySetOf
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.dsl.fixture.BehaviourTest
import it.unibo.pulvreakt.core.dsl.fixture.CommTest
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
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.ComponentNotRegistered
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.EmptyDeploymentConfiguration
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.InvalidReconfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.EmptyDeviceConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.EmptySystemConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.UnspecifiedCapabilities
import it.unibo.pulvreakt.dsl.model.DeviceReconfigurationRule
import it.unibo.pulvreakt.dsl.model.NewConfiguration
import it.unibo.pulvreakt.dsl.pulverization

class PulverizationDslTest : StringSpec({
    "The DSL should prevent with an error the creation of an empty system" {
        val config = pulverization {
            system { }
        }
        config shouldBe Either.Left(nonEmptyListOf(EmptyDeploymentConfiguration, EmptySystemConfiguration))
    }
    "The DSL should prevent with an error the creation of an empty device" {
        val config = pulverization {
            system {
                extendedLogicDevice("my device") { }
            }
        }
        config shouldBe Either.Left(nonEmptyListOf(EmptyDeploymentConfiguration, EmptyDeviceConfiguration))
    }
    "The DSL should raise an error if no capability is specified for a component" {
        val config = pulverization {
            system {
                extendedLogicDevice("my device") {
                    withComponent<TestComponent1>()
                }
            }
            deployment(testInfrastructure, TestProtocol()) {
                device("my device") {
                    TestComponent1() startsOn smartphoneHost
                }
            }
        }
        config shouldBe Either.Left(nonEmptyListOf(UnspecifiedCapabilities(ComponentRef.create<TestComponent1>())))
    }
    "The DSL should raise an error if the deployment do not match the system configuration for the same device" {
        val config = pulverization {
            system {
                extendedLogicDevice("my device") {
                    val component1 = withComponent<TestComponent1>()
                    val component2 = withComponent<TestComponent2>()
                    component1 requires embeddedDeviceCapability
                    component2 requires embeddedDeviceCapability
                    component1 wiredTo component2
                    component2 wiredTo component1
                }
            }
            deployment(testInfrastructure, TestProtocol()) {
                device("my device") { }
            }
        }
        config shouldBe Either.Left(
            nonEmptyListOf(
                ComponentNotRegistered(ComponentRef.create<TestComponent1>()),
                ComponentNotRegistered(ComponentRef.create<TestComponent2>()),
            ),
        )
    }
    "The DSL should raise an error if a reconfiguration rule move a component into an invalid host" {
        val config = pulverization {
            system {
                extendedLogicDevice("my device") {
                    val component1 = withComponent<TestComponent1>()
                    val component2 = withComponent<TestComponent2>()
                    component1 requires embeddedDeviceCapability
                    component2 requires embeddedDeviceCapability
                    component1 wiredTo component2
                    component2 wiredTo component1
                }
            }
            deployment(testInfrastructure, TestProtocol()) {
                device("my device") {
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
            system {
                logicDevice("my device") {
                    val component1 = withBehaviour<BehaviourTest>()
                    component1 requires embeddedDeviceCapability
                }
            }
            deployment(testInfrastructure, TestProtocol()) {
                device("my device") {
                    TestComponent1() startsOn smartphoneHost
                }
            }
        }
        config.isLeft() shouldBe true
        config shouldBe Either.Left(
            nonEmptyListOf(
                DeploymentConfigurationError.UnknownComponent(ComponentRef.create<TestComponent1>()),
            ),
        )
    }
    "The DSL should raise an error is an unknown component is moved in a reconfiguration rule" {
        val config = pulverization {
            system {
                logicDevice("my device") {
                    val component1 = withBehaviour<BehaviourTest>()
                    component1 requires embeddedDeviceCapability
                }
            }
            deployment(testInfrastructure, TestProtocol()) {
                device("my device") {
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
                DeploymentConfigurationError.UnknownComponent(ComponentRef.create<TestComponent1>()),
            ),
        )
    }
    "The DSL should produce the configuration when properly used" {
        val configResult = pulverization {
            system {
                extendedLogicDevice("my device") {
                    val component1 = withComponent<TestComponent1>()
                    val component2 = withComponent<TestComponent2>()
                    component1 requires nonEmptySetOf(embeddedDeviceCapability, serverCapability)
                    component2 requires embeddedDeviceCapability
                    component1 wiredTo component2
                    component2 wiredTo component1
                }
            }
            deployment(testInfrastructure, TestProtocol()) {
                device("my device") {
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
    "The DSL should configure a device with the classical model" {
        val configResult = pulverization {
            system {
                logicDevice("my device") {
                    val component1 = withBehaviour<BehaviourTest>()
                    val component2 = withCommunication<CommTest>()
                    component1 requires nonEmptySetOf(embeddedDeviceCapability, serverCapability)
                    component2 requires embeddedDeviceCapability
                }
            }
            deployment(testInfrastructure, TestProtocol()) {
                device("my device") {
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
                    ComponentRef.create<BehaviourTest>(ComponentType.Behaviour) to setOf(ComponentRef.create<CommTest>(ComponentType.Communication)),
                    ComponentRef.create<CommTest>(ComponentType.Communication) to setOf(ComponentRef.create<BehaviourTest>(ComponentType.Behaviour)),
                )
                deviceSpec.requiredCapabilities shouldBe mapOf(
                    ComponentRef.create<BehaviourTest>(ComponentType.Behaviour) to nonEmptySetOf(embeddedDeviceCapability, serverCapability),
                    ComponentRef.create<CommTest>(ComponentType.Communication) to nonEmptySetOf(embeddedDeviceCapability),
                )
            }
        }
    }
    "The DSL should admit a mixed configuration with simple device and extended one" {
        val configResult = pulverization {
            system {
                logicDevice("device 1") {
                    val component1 = withBehaviour<BehaviourTest>()
                    val component2 = withCommunication<CommTest>()
                    component1 requires nonEmptySetOf(embeddedDeviceCapability, serverCapability)
                    component2 requires embeddedDeviceCapability
                }
                extendedLogicDevice("device 2") {
                    val component1 = withComponent<TestComponent1>()
                    val component2 = withComponent<TestComponent2>()
                    component1 requires embeddedDeviceCapability
                    component2 requires embeddedDeviceCapability
                    component1 wiredTo component2
                    component2 wiredTo component1
                }
            }
            deployment(testInfrastructure, TestProtocol()) {
                device("device 1") {
                    BehaviourTest() startsOn smartphoneHost
                    CommTest() startsOn smartphoneHost
                }
                device("device 2") {
                    TestComponent1() startsOn smartphoneHost
                    TestComponent2() startsOn smartphoneHost
                }
            }
        }
        configResult.isRight() shouldBe true
        configResult.getOrNull()!!.let { config ->
            config["device 1"] shouldNotBe null
            config["device 2"] shouldNotBe null
        }
    }
})
