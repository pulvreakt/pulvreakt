package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ReconfigurationRules

/**
 * Scope class for configuring reconfiguration rules.
 */
class ReconfigurationRulesScope {
    private var allReconfigurationRules = ReconfigurationRules(emptySet())

    /**
     * Configures the reconfiguration rules on device.
     */
    fun onDevice(config: OnDeviceScope.() -> Unit) {
        val deviceRulesScope = OnDeviceScope().apply(config)
        val newRules = allReconfigurationRules.deviceReconfigurationRules + deviceRulesScope.generate()
        allReconfigurationRules = allReconfigurationRules.copy(
            deviceReconfigurationRules = newRules,
        )
    }

    internal fun generate(): ReconfigurationRules = allReconfigurationRules
}
