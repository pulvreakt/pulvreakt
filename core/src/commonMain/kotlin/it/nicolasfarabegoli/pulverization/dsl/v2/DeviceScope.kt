package it.nicolasfarabegoli.pulverization.dsl.v2

import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.dsl.Tier
import it.nicolasfarabegoli.pulverization.dsl.v2.model.DeviceDeploymentMap
import it.nicolasfarabegoli.pulverization.dsl.v2.model.LogicalDeviceSpecification
import it.nicolasfarabegoli.pulverization.dsl.v2.model.PartialDeploymentMap

/**
 * DSL scope for configuring a device with a [deviceName].
 */
class DeviceScope(private val deviceName: String) {
    private var allocationMap: DeviceDeploymentMap = DeviceDeploymentMap(emptyMap(), emptyMap())

    /**
     * Utility method used for set up the components belonging to a device.
     */
    infix fun Set<PulverizedComponentType>.and(otherComponent: PulverizedComponentType): Set<PulverizedComponentType> =
        this + otherComponent

    /**
     * Utility method used for set up the components belonging to a device.
     */
    infix fun PulverizedComponentType.and(otherComponent: PulverizedComponentType): Set<PulverizedComponentType> =
        setOf(this, otherComponent)

    /**
     * Utility method for defining where the specified component can be deployed on.
     */
    infix fun Set<PulverizedComponentType>.deployableOn(tier: Tier): PartialDeploymentMap {
        return PartialDeploymentMap(this, setOf(tier))
    }

    /**
     * Utility method for defining where the specified component can be deployed on.
     */
    infix fun Set<PulverizedComponentType>.deployableOn(tier: Set<Tier>): PartialDeploymentMap {
        return PartialDeploymentMap(this, tier)
    }

    /**
     * Utility method for defining where the specified component can be deployed on.
     */
    infix fun PulverizedComponentType.deployableOn(tiers: Set<Tier>): PartialDeploymentMap {
        return PartialDeploymentMap(setOf(this), tiers)
    }

    /**
     * Utility method for defining where the specified component can be deployed on.
     */
    infix fun PulverizedComponentType.deployableOn(tiers: Tier): PartialDeploymentMap {
        return PartialDeploymentMap(setOf(this), setOf(tiers))
    }

    /**
     * Utility method for defining multiple tier where component(s) can be deployed on.
     */
    infix fun Tier.or(otherTier: Tier): Set<Tier> = setOf(this, otherTier)

    /**
     * Utility method for defining multiple tier where component(s) can be deployed on.
     */
    infix fun Set<Tier>.or(otherTier: Tier): Set<Tier> = this + otherTier

    /**
     * Utility method for defining where the component is deployed on the startup of the system.
     */
    infix fun PartialDeploymentMap.startsOn(startTier: Tier) {
        val allocComps = allocationMap.allocationComponents + components.associateWith { tier }
        val startupTier = allocationMap.startsOn + components.associateWith { startTier }
        allocationMap = DeviceDeploymentMap(allocComps, startupTier)
    }

    internal fun generate(): LogicalDeviceSpecification =
        LogicalDeviceSpecification(deviceName, allocationMap, allocationMap.allocationComponents.keys)
}
