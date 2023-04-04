package it.nicolasfarabegoli.pulverization.dsl.v2.model

import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.dsl.Tier

/**
 * Partial deployment configuration where only partial [components] are configured with their respective [tier].
 * Note: this class is used internally by the DSL to build the _allocation map_ incrementally and **MUST** not used
 * directly.
 */
data class PartialDeploymentMap(val components: Set<PulverizedComponentType>, val tier: Set<Tier>)

/**
 * Definition of the _allocation map_ where [componentsAllocations] defines for each component where it can be deployed
 * and [componentsStartup] defines where each component is started on which tier.
 */
data class DeviceAllocationMap(
    val componentsAllocations: Map<PulverizedComponentType, Set<Tier>>,
    val componentsStartup: Map<PulverizedComponentType, Tier>,
)

/**
 * Configuration of a device called [deviceName] with its [allocationMap] and its [components].
 */
data class LogicalDeviceSpecification(
    val deviceName: String,
    val allocationMap: DeviceAllocationMap,
    val components: Set<PulverizedComponentType>,
)

/**
 * Specification of the system where all the device are configured with [devicesConfiguration].
 */
data class SystemSpecification(val devicesConfiguration: Set<LogicalDeviceSpecification>) {
    /**
     * Get the [LogicalDeviceSpecification] belonging to a [device].
     * If no device is found, null is returned.
     */
    fun getConfigurationByDeviceOrNull(device: String): LogicalDeviceSpecification? =
        devicesConfiguration.firstOrNull { it.deviceName == device }

    /**
     * Similar to [getConfigurationByDeviceOrNull] but raise an exception if no [device] is found in the configuration.
     */
    fun getConfigurationByDevice(device: String): LogicalDeviceSpecification =
        getConfigurationByDeviceOrNull(device) ?: error("Unable to find the $device in the configuration")
}
