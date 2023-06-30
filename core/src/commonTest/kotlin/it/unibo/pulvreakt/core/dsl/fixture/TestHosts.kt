package it.unibo.pulvreakt.core.dsl.fixture

import arrow.core.nonEmptySetOf
import it.unibo.pulvreakt.core.infrastructure.Host

val smartphoneHost = Host("smartphone", embeddedDeviceCapability)
val serverHost = Host("server", serverCapability)

val testInfrastructure = nonEmptySetOf(smartphoneHost, serverHost)
