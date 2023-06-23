package it.unibo.pulvreakt.core.dsl.fixture

import arrow.core.nonEmptySetOf
import it.unibo.pulvreakt.core.infrastructure.Host

val smartphoneHost = Host("smartphone", embeddedDeviceCapability)

val testInfrastructure = nonEmptySetOf(smartphoneHost)
