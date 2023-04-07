package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ReconfigurationRules

class ReconfigurationRulesScope {
    private var allReconfigurationRules = ReconfigurationRules(emptySet())

    fun onDevice(config: OnDeviceScope.() -> Unit) {
        val deviceRulesScope = OnDeviceScope().apply(config)
        val newRules = allReconfigurationRules.deviceReconfigurationRules + deviceRulesScope.generate()
        allReconfigurationRules = allReconfigurationRules.copy(
            deviceReconfigurationRules = newRules,
        )
    }

    internal fun generate(): ReconfigurationRules = allReconfigurationRules
}
