package it.nicolasfarabegoli.pulverization.dsl.v2

import it.nicolasfarabegoli.pulverization.dsl.v2.model.LogicalDeviceSpecification
import it.nicolasfarabegoli.pulverization.dsl.v2.model.SystemSpecification

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
