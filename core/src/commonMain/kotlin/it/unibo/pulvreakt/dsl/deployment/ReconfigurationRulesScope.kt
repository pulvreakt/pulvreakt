package it.unibo.pulvreakt.dsl.deployment

import it.unibo.pulvreakt.dsl.model.DeviceReconfigurationRule

/**
 * Scope for the reconfiguration rules.
 */
class ReconfigurationRulesScope {

    /**
     * Specifies the reconfiguration rules associated to the device.
     */
    fun onDevice(config: OnDeviceScope.() -> Unit): Nothing = TODO()

    internal fun generate(): Set<DeviceReconfigurationRule> = TODO()
}
