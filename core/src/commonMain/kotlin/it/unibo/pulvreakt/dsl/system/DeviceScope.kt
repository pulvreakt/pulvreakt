package it.unibo.pulvreakt.dsl.system

import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.dsl.system.model.Capability
import it.unibo.pulvreakt.dsl.system.model.LogicalDeviceSpecification

/**
 * Configure the definition of the logical device with a [deviceName].
 */
class DeviceScope(private val deviceName: String) {
    private val allocationMap: MutableMap<ComponentType, Set<Capability>> = mutableMapOf()

    /**
     * Utility DSL function to group different [Component]s.
     */
    infix fun ComponentType.and(other: ComponentType): Set<ComponentType> = setOf(this, other)

    /**
     * Utility DSL function to group different [Component]s.
     */
    infix fun Set<ComponentType>.and(other: ComponentType): Set<ComponentType> = this + other

    /**
     * Register that the [Component] requires a [capability].
     */
    infix fun ComponentType.deployableOn(capability: Capability) {
        allocationMap[this] = setOf(capability)
    }

    /**
     * Register that the [Component] requires a set of [capabilities].
     */
    infix fun ComponentType.deployableOn(capabilities: Set<Capability>) {
        allocationMap[this] = capabilities
    }

    /**
     * Register that the [Component] requires a [capability].
     */
    infix fun Set<ComponentType>.deployableOn(capability: Capability) {
        allocationMap.putAll(this.associateWith { setOf(capability) })
    }

    /**
     * Register that the [Component] requires a set of [capabilities].
     */
    infix fun Set<ComponentType>.deployableOn(capabilities: Set<Capability>) {
        allocationMap.putAll(this.associateWith { capabilities })
    }

    internal fun generate(): LogicalDeviceSpecification = LogicalDeviceSpecification(deviceName, allocationMap)
}
