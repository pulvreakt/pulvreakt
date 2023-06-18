package it.unibo.pulvreakt.dsl.deployment

import it.unibo.pulvreakt.dsl.model.LogicalDeviceSpecification

/**
 * Scope for the reconfiguration rules.
 */
class ReconfigurationRulesScope(private val logicalDeviceSpecification: LogicalDeviceSpecification) {

    /**
     * Specifies the reconfiguration rules associated to the device.
     */
    fun onDevice(config: OnDeviceScope.() -> Unit): Nothing = TODO()

    internal fun generate(): Nothing = TODO()
}
