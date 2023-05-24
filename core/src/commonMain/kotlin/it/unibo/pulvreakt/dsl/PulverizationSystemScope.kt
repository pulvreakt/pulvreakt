package it.unibo.pulvreakt.dsl

import it.unibo.pulvreakt.dsl.model.LogicalDeviceSpecification
import it.unibo.pulvreakt.dsl.model.SystemSpecification

/**
 * DSL pulverization marker.
 */
@DslMarker
annotation class PulverizationDsl

/**
 * DSL scope for configuring the pulverized system.
 */
@PulverizationDsl
class PulverizationSystemScope {
    private var devices: Set<LogicalDeviceSpecification> = emptySet()

    /**
     * Configures a device with [name].
     */
    fun device(name: String, setup: DeviceScope.() -> Unit) {
        val deviceScope = DeviceScope(name).apply(setup)
        devices = devices + deviceScope.generate()
    }

    internal fun generate(): SystemSpecification = SystemSpecification(devices)
}