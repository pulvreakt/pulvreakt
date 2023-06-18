package it.unibo.pulvreakt.dsl.model

import it.unibo.pulvreakt.core.component.ComponentOps

/**
 * Specification of a logical device with a [deviceName] and [componentsRequiredCapabilities].
 */
data class LogicalDeviceSpecification(
    val deviceName: String,
    val componentsRequiredCapabilities: Map<ComponentOps<*>, Set<Capability>>,
)
