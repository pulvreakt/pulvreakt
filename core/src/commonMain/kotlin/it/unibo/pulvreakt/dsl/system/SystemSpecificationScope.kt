package it.unibo.pulvreakt.dsl.system

import it.unibo.pulvreakt.dsl.system.model.LogicalDeviceSpecification
import it.unibo.pulvreakt.dsl.system.model.SystemSpecification

class SystemSpecificationScope {
    private val deviceSpecifications = mutableSetOf<LogicalDeviceSpecification>()

    fun device(name: String, config: DeviceScope.() -> Unit) {
        val deviceScope = DeviceScope(name).apply(config)
        deviceSpecifications.add(deviceScope.generate())
    }

    internal fun generate(): SystemSpecification = SystemSpecification(deviceSpecifications.toSet())
}
