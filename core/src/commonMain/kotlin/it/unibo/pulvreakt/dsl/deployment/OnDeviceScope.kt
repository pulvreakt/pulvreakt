package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.core.reconfiguration.event.ReconfigurationEvent
import it.unibo.pulvreakt.dsl.deployment.model.DeviceReconfigurationRule

class OnDeviceScope {
    private val reconfigurationRules = mutableListOf<Either<String, DeviceReconfigurationRule>>()

    infix fun ReconfigurationEvent<*>.reconfigures(config: ReconfigurationComponentScope.() -> Unit) {
        val reconfigurationScope = ReconfigurationComponentScope().apply(config)
        val reconfiguration = reconfigurationScope.generate()
        reconfigurationRules.add(reconfiguration.map { DeviceReconfigurationRule(this, it) })
    }

    internal fun generate(): Either<String, List<DeviceReconfigurationRule>> = either { reconfigurationRules.bindAll() }
}
