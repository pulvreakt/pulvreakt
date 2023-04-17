package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.runtime.dsl.model.DeviceReconfigurationRule
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.ReconfigurationEvent
import kotlinx.coroutines.flow.Flow

/**
 * Scope class for configuring a reconfiguration rule on device.
 */
class OnDeviceScope {
    private var onDeviceReconfigurationRules = emptySet<DeviceReconfigurationRule>()

    /**
     * Configure the [ReconfigurationEvent] based on the [event] and the [predicate] that must be true to reconfigure
     * the device.
     */
    fun <P : Any> on(
        event: Flow<P>,
        predicate: (P) -> Boolean,
    ): ReconfigurationEvent<P> = ReconfigurationEvent(event, predicate)

    /**
     * Given the [ReconfigurationEvent] configure which reconfiguration should occur.
     */
    infix fun <P : Any> ReconfigurationEvent<P>.reconfigures(
        config: ReconfigurationComponentScope.() -> Unit,
    ) {
        val reconfigurationScope = ReconfigurationComponentScope().apply(config)
        val reconfiguration = reconfigurationScope.generate()
        onDeviceReconfigurationRules = onDeviceReconfigurationRules + DeviceReconfigurationRule(this, reconfiguration)
    }

    internal fun generate(): Set<DeviceReconfigurationRule> = onDeviceReconfigurationRules
}
