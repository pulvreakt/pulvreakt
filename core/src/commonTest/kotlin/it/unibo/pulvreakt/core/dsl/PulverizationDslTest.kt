package it.unibo.pulvreakt.core.dsl

import arrow.core.Either
import arrow.core.nonEmptyListOf
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.EmptyDeploymentConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.EmptyDeviceConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.EmptySystemConfiguration
import it.unibo.pulvreakt.dsl.pulverization

class PulverizationDslTest : StringSpec({
    "The DSL should prevent with an error the creation of an empty system" {
        val config = pulverization {
            system { }
        }
        config shouldBe Either.Left(nonEmptyListOf(EmptyDeploymentConfiguration, EmptySystemConfiguration))
    }
    "The DSL should prevent with an error the creation of an empty device" {
        val config1 = pulverization {
            system {
                extendedLogicDevice("my device") { }
            }
        }
        config1 shouldBe Either.Left(nonEmptyListOf(EmptyDeploymentConfiguration, EmptyDeviceConfiguration))
    }
})
