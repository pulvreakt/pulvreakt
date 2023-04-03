package it.nicolasfarabegoli.pulverization.dsl.v2.model

import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.dsl.Tier

/**
 * Partial deployment configuration where only partial [components] and configured with their respective [tier].
 */
data class PartialDeploymentMap(val components: Set<PulverizedComponentType>, val tier: Set<Tier>)

/**
 * Definition of the _allocation map_ where [allocationComponents] defines for each component where it can be deployed
 * and [startsOn] defines where each component is started.
 */
data class DeviceDeploymentMap(
    val allocationComponents: Map<PulverizedComponentType, Set<Tier>>,
    val startsOn: Map<PulverizedComponentType, Tier>,
)

/**
 * Configuration of a device called [deviceName] with its [deploymentMap] and its [components].
 */
data class LogicalDeviceSpecification(
    val deviceName: String,
    val deploymentMap: DeviceDeploymentMap,
    val components: Set<PulverizedComponentType>,
)
