package it.unibo.pulvreakt.dsl.model.old

import it.unibo.pulvreakt.core.component.ComponentOps
import it.unibo.pulvreakt.dsl.model.Capability

/**
 * Specification of a logical device with a [deviceName] and [componentsRequiredCapabilities].
 */
data class LogicalDeviceSpecification(
    val deviceName: String,
    val componentsRequiredCapabilities: Map<ComponentOps<*>, Set<Capability>>,
)
