package it.unibo.pulvreakt.dsl.system

import arrow.core.Either
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

    internal fun generate(): Either<String, SystemSpecification> = SystemSpecification(deviceSpecifications)
}
