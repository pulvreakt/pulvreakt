package it.unibo.pulvreakt.core.dsl

import arrow.core.Either
import arrow.core.nonEmptyListOf
import arrow.core.nonEmptySetOf
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import it.unibo.pulvreakt.core.dsl.fixture.TestCommunicator
import it.unibo.pulvreakt.core.dsl.fixture.TestComponent1
import it.unibo.pulvreakt.core.dsl.fixture.TestComponent2
import it.unibo.pulvreakt.core.dsl.fixture.TestReconfigurationEvent1
import it.unibo.pulvreakt.core.dsl.fixture.TestReconfigurationEvent2
import it.unibo.pulvreakt.core.dsl.fixture.TestReconfigurator
import it.unibo.pulvreakt.core.dsl.fixture.embeddedDeviceCapability
import it.unibo.pulvreakt.core.dsl.fixture.serverCapability
import it.unibo.pulvreakt.core.dsl.fixture.serverHost
import it.unibo.pulvreakt.core.dsl.fixture.smartphoneHost
import it.unibo.pulvreakt.core.dsl.fixture.testInfrastructure
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.ComponentNotRegistered
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.EmptyDeploymentConfiguration
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.InvalidReconfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.EmptyDeviceConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.EmptySystemConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.UnspecifiedCapabilities
import it.unibo.pulvreakt.dsl.model.ComponentType.Companion.ctypeOf
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
            deployment(testInfrastructure, { TestCommunicator() }, { TestReconfigurator() }) {
                device("my device") {
                    TestComponent1() startsOn smartphoneHost
                }
            }
        }
        config shouldBe Either.Left(nonEmptyListOf(UnspecifiedCapabilities(ctypeOf<TestComponent1>())))
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
            deployment(testInfrastructure, { TestCommunicator() }, { TestReconfigurator() }) {
                device("my device") { }
            }
        }
        config shouldBe Either.Left(
            nonEmptyListOf(
                ComponentNotRegistered(ctypeOf<TestComponent1>()),
                ComponentNotRegistered(ctypeOf<TestComponent2>()),
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
            deployment(testInfrastructure, { TestCommunicator() }, { TestReconfigurator() }) {
                device("my device") {
                    TestComponent1() startsOn smartphoneHost
                    TestComponent2() startsOn smartphoneHost
                    reconfigurationRules {
                        onDevice {
                            TestReconfigurationEvent1() reconfigures (ctypeOf<TestComponent1>() movesTo serverHost)
                            TestReconfigurationEvent2() reconfigures (ctypeOf<TestComponent2>() movesTo serverHost)
                        }
                    }
                }
            }
        }
        config.isLeft() shouldBe true
        config shouldBe Either.Left(
            nonEmptyListOf(
                InvalidReconfiguration(ctypeOf<TestComponent1>(), serverHost),
                InvalidReconfiguration(ctypeOf<TestComponent2>(), serverHost),
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
            deployment(testInfrastructure, { TestCommunicator() }, { TestReconfigurator() }) {
                device("my device") {
                    TestComponent1() startsOn smartphoneHost
                    TestComponent2() startsOn smartphoneHost
                    reconfigurationRules {
                        onDevice {
                            TestReconfigurationEvent1() reconfigures (ctypeOf<TestComponent1>() movesTo serverHost)
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
                    ctypeOf<TestComponent1>() to setOf(ctypeOf<TestComponent2>()),
                    ctypeOf<TestComponent2>() to setOf(ctypeOf<TestComponent1>()),
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
                            NewConfiguration(ctypeOf<TestComponent1>(), serverHost),
                        ),
                    )
                }
            }
        } ?: error("The configuration should be present")
    }
})
