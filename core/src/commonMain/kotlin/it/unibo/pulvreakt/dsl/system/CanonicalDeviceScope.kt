package it.unibo.pulvreakt.dsl.system

import arrow.core.Either
import arrow.core.Nel
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError
import it.unibo.pulvreakt.dsl.model.DeviceStructure

class CanonicalDeviceScope(private val deviceName: String) {
    internal fun generate(): Either<Nel<SystemConfigurationError>, DeviceStructure> = TODO()
}
