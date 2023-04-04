package it.nicolasfarabegoli.pulverization.dsl.v2

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Capability
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.v2.model.LogicalDeviceSpecification

/**
 * DSL scope for configuring a device with a [deviceName].
 */
class DeviceScope(private val deviceName: String) {
    private var allocationMap = emptyMap<ComponentType, Set<Capability>>()

    /**
     * Utility method for defining where the specified component can be deployed on.
     */
    infix fun Set<ComponentType>.deployableOn(tier: Capability) = deployableOn(setOf(tier))

    /**
     * Utility method for defining where the specified component can be deployed on.
     */
    infix fun Set<ComponentType>.deployableOn(tiers: Set<Capability>) {
        allocationMap = allocationMap + this.associateWith { tiers }
    }

    /**
     * Utility method for defining where the specified component can be deployed on.
     */
    infix fun ComponentType.deployableOn(tier: Capability) = deployableOn(setOf(tier))

    /**
     * Utility method for defining where the specified component can be deployed on.
     */
    infix fun ComponentType.deployableOn(tiers: Set<Capability>) {
        allocationMap = allocationMap + (this to tiers)
    }

    /**
     * Utility method used for set up the components belonging to a device.
     */
    infix fun <C> Set<C>.and(otherComponent: C): Set<C> = this + otherComponent

    /**
     * Utility method used for set up the components belonging to a device.
     */
    infix fun <C> C.and(otherComponent: C): Set<C> = setOf(this, otherComponent)

    internal fun generate(): LogicalDeviceSpecification =
        LogicalDeviceSpecification(deviceName, allocationMap, allocationMap.keys)
}
