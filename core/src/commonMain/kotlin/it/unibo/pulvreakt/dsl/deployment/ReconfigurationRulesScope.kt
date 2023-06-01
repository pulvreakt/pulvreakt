package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import it.unibo.pulvreakt.dsl.deployment.model.DeviceReconfigurationRule
import it.unibo.pulvreakt.dsl.deployment.model.ReconfigurationRules

class ReconfigurationRulesScope {
    private lateinit var deviceRules: Either<String, List<DeviceReconfigurationRule>>

    fun onDevice(config: OnDeviceScope.() -> Unit) {
        val onDeviceScope = OnDeviceScope().apply(config)
        deviceRules = onDeviceScope.generate()
    }

    internal fun generate(): Either<String, ReconfigurationRules> = either {
        ensure(::deviceRules.isInitialized) { "No device reconfiguration rules are registered" }
        ReconfigurationRules(deviceRules.map { it.toSet() }.bind())
    }
}
