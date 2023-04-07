package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.DeviceReconfigurationRule
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ReconfigurationEvent
import kotlinx.coroutines.flow.Flow

class OnDeviceScope {
    private var onDeviceReconfigurationRules = emptySet<DeviceReconfigurationRule>()

    fun <P : Any> on(
        event: Flow<P>,
        predicate: (P) -> Boolean,
    ): ReconfigurationEvent<P> = ReconfigurationEvent(event, predicate)

    infix fun <P : Any> ReconfigurationEvent<P>.reconfigures(
        config: ReconfigurationComponentScope.() -> Unit,
    ) {
        val reconfigurationScope = ReconfigurationComponentScope().apply(config)
        val reconfiguration = reconfigurationScope.generate()
        onDeviceReconfigurationRules = onDeviceReconfigurationRules + DeviceReconfigurationRule(this, reconfiguration)
    }

    internal fun generate(): Set<DeviceReconfigurationRule> = onDeviceReconfigurationRules
}
