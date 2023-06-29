package it.unibo.pulvreakt.core.dsl

import arrow.core.Either
import arrow.core.nonEmptyListOf
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.core.dsl.fixture.TestCommunicator
import it.unibo.pulvreakt.core.dsl.fixture.TestComponent1
import it.unibo.pulvreakt.core.dsl.fixture.TestComponent2
import it.unibo.pulvreakt.core.dsl.fixture.TestReconfigurator
import it.unibo.pulvreakt.core.dsl.fixture.embeddedDeviceCapability
import it.unibo.pulvreakt.core.dsl.fixture.smartphoneHost
import it.unibo.pulvreakt.core.dsl.fixture.testInfrastructure
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.ComponentNotRegistered
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.EmptyDeploymentConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.EmptyDeviceConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.EmptySystemConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.UnspecifiedCapabilities
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
                    component<TestComponent1>()
                }
            }
            deployment(testInfrastructure, { TestCommunicator() }, { TestReconfigurator() }) {
                device("my device") {
                    TestComponent1() startsOn smartphoneHost
                }
            }
        }
        config shouldBe Either.Left(nonEmptyListOf(UnspecifiedCapabilities("TestComponent1")))
    }
    "The DSL should raise an error if the deployment do not match the system configuration for the same device" {
        val config = pulverization {
            system {
                extendedLogicDevice("my device") {
                    val component1 = component<TestComponent1>()
                    val component2 = component<TestComponent2>()
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
            nonEmptyListOf(ComponentNotRegistered("TestComponent1"), ComponentNotRegistered("TestComponent2")),
        )
    }
    "The DSL should raise an error if a reconfiguration rule move a component into an invalid host" {
        val config = pulverization {
            system {
                extendedLogicDevice("my device") {
                    val component1 = component<TestComponent1>()
                    val component2 = component<TestComponent2>()
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
                }
            }
        }
        config.isRight() shouldBe true
    }
})
