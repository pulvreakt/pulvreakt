package it.unibo.pulvreakt.dsl.system

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate
import arrow.core.toNonEmptyListOrNull
import arrow.core.toNonEmptySetOrNull
import it.unibo.pulvreakt.dsl.system.errors.SystemDslError
import it.unibo.pulvreakt.dsl.system.errors.SystemDslError.DuplicateDeviceName
import it.unibo.pulvreakt.dsl.system.model.LogicalDeviceSpecification
import it.unibo.pulvreakt.dsl.system.model.SystemSpecification

/**
 * System DSL scope for configuring logical devices.
 */
class SystemSpecificationScope {
    private val deviceSpecifications = mutableListOf<LogicalDeviceSpecification>()

    /**
     * Configures a new logical device.
     */
    fun device(name: String, config: DeviceScope.() -> Unit) {
        val deviceScope = DeviceScope(name).apply(config)
        deviceSpecifications.add(deviceScope.generate())
    }

    private fun Raise<NonEmptyList<DuplicateDeviceName>>.validate(): List<LogicalDeviceSpecification> {
        val duplicates =
            deviceSpecifications.filter { elem -> deviceSpecifications.count { it.deviceName == elem.deviceName } > 1 }.toNonEmptySetOrNull()
        duplicates?.let { raise(duplicates.toNonEmptyListOrNull()!!.map { DuplicateDeviceName(it.deviceName) }) }
        return deviceSpecifications
    }

    internal fun generate(): Either<NonEmptyList<SystemDslError>, SystemSpecification> = either {
        zipOrAccumulate(
            { ensure(deviceSpecifications.isNotEmpty()) { SystemDslError.EmptyConfiguration } },
            { validate() },
        ) { _, devices -> SystemSpecification(devices.toNonEmptySetOrNull()!!) }
    }
}
