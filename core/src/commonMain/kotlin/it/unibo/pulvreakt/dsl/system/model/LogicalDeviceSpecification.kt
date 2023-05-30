package it.unibo.pulvreakt.dsl.system.model

import it.unibo.pulvreakt.core.component.ComponentType

/**
 * Specification of a logical device with a [deviceName] and [componentsRequiredCapabilities].
 */
data class LogicalDeviceSpecification(
    val deviceName: String,
    val componentsRequiredCapabilities: Map<ComponentType, Set<Capability>>,
)
