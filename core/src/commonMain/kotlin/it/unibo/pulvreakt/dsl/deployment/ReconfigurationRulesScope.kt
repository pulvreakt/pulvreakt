package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import it.unibo.pulvreakt.dsl.deployment.model.DeviceReconfigurationRule
import it.unibo.pulvreakt.dsl.deployment.model.ReconfigurationRules

/**
 * Scope for the reconfiguration rules.
 */
class ReconfigurationRulesScope {
    private var deviceRules: Either<NonEmptyList<String>, List<DeviceReconfigurationRule>>? = null

    /**
     * Specifies the reconfiguration rules associated to the device.
     */
    fun onDevice(config: OnDeviceScope.() -> Unit) {
        val onDeviceScope = OnDeviceScope().apply(config)
        deviceRules = onDeviceScope.generate()
    }

    internal fun generate(): Either<NonEmptyList<String>, ReconfigurationRules> = either {
        val deviceRules = deviceRules?.map { it.toSet() }?.bind()
        ReconfigurationRules(deviceRules ?: emptySet())
    }
}
