package it.unibo.pulvreakt.dsl.system

import arrow.core.Either
import arrow.core.Nel
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate
import arrow.core.toNonEmptyListOrNull
import arrow.core.toNonEmptySetOrNull
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.DuplicateDeviceName
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.EmptySystemConfiguration
import it.unibo.pulvreakt.dsl.model.ConfiguredDeviceStructure
import it.unibo.pulvreakt.dsl.model.DeviceStructure

/**
 * System DSL scope for configuring logical devices.
 */
class SystemSpecificationScope {
    private val devices = mutableListOf<Either<Nel<SystemConfigurationError>, DeviceStructure>>()

    fun extendedLogicDevice(name: String, config: ExtendedDeviceScope.() -> Unit) {
        val deviceScope = ExtendedDeviceScope(name).apply(config)
        devices.add(deviceScope.generate())
    }

    fun logicDevice(name: String, config: CanonicalDeviceScope.() -> Unit) {
        val deviceScope = CanonicalDeviceScope(name).apply(config)
        devices.add(deviceScope.generate())
    }

    private fun Raise<Nel<DuplicateDeviceName>>.validate(l: List<DeviceStructure>): List<DeviceStructure> {
        val duplicates = l.filter { elem -> l.count { it.deviceName == elem.deviceName } > 1 }.toNonEmptySetOrNull()
        duplicates?.let { raise(duplicates.toNonEmptyListOrNull()!!.map { DuplicateDeviceName(it.deviceName) }) }
        return l
    }

    internal fun generate(): Either<Nel<SystemConfigurationError>, ConfiguredDeviceStructure> = either {
        zipOrAccumulate(
            { ensure(devices.isNotEmpty()) { EmptySystemConfiguration } },
            { validate(devices.bindAll()) },
        ) { _, config -> config.toNonEmptySetOrNull()!! }
    }
}
