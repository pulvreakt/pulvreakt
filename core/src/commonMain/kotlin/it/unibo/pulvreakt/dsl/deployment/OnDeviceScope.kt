package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.mapOrAccumulate
import it.unibo.pulvreakt.core.reconfiguration.event.ReconfigurationEvent
import it.unibo.pulvreakt.dsl.deployment.model.DeviceReconfigurationRule

/**
 * Scope for the reconfiguration of a device.
 */
class OnDeviceScope {
    private val reconfigurationRules = mutableListOf<Either<String, DeviceReconfigurationRule>>()

    /**
     * Specifies the reconfiguration associated to the [ReconfigurationEvent].
     */
    infix fun ReconfigurationEvent<*>.reconfigures(config: ReconfigurationComponentScope.() -> Unit) {
        val reconfigurationScope = ReconfigurationComponentScope().apply(config)
        val reconfiguration = reconfigurationScope.generate()
        reconfigurationRules.add(reconfiguration.map { DeviceReconfigurationRule(this, it) })
    }

    internal fun generate(): Either<NonEmptyList<String>, List<DeviceReconfigurationRule>> = reconfigurationRules.mapOrAccumulate { e -> e.bind() }
}
