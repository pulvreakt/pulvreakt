package it.unibo.pulvreakt.modularization

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.modularization.api.DeploymentUnit.Companion.registerModule
import it.unibo.pulvreakt.modularization.api.deploymentUnit
import it.unibo.pulvreakt.modularization.fixture.EmbeddedDeviceHost
import it.unibo.pulvreakt.modularization.fixture.FakeHostDiscover
import it.unibo.pulvreakt.modularization.fixture.TemperatureConditioner
import it.unibo.pulvreakt.modularization.fixture.TimeScheduler
import it.unibo.pulvreakt.modularization.fixture.configuration

class DeploymentUnitTest : FreeSpec(
    {
        "A deployment unit" - {
            "should raise an error if a module cannot be deployed locally or on a neighbour host" {
                val deploymentUnit = deploymentUnit(configuration, EmbeddedDeviceHost(), TimeScheduler(), FakeHostDiscover()) {
                    registerModule(configuration.keys.first() as TemperatureConditioner)
                }
                deploymentUnit.setup().isLeft() shouldBe true
            }
        }
    },
)
