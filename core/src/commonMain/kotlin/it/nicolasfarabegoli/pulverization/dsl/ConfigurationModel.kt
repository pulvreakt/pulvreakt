package it.nicolasfarabegoli.pulverization.dsl

import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType

/**
 * Type of _tier_ where a [DeploymentUnit] could be deployed.
 */
sealed interface Tier

/**
 * Cloud tier.
 */
object Cloud : Tier

/**
 * Edge tier.
 */
object Edge : Tier

/**
 * Device tier.
 */
object Device : Tier

/**
 * Represents the configuration of a deployment unit.
 * The configuration contains all the [deployableComponents] and a reference to the tier [where] the deployment unit
 * should be deployed.
 */
data class DeploymentUnit(val deployableComponents: Set<PulverizedComponentType>, val where: Tier)

/**
 * Represents the configuration of a **Logical device**.
 * The configuration is identified by a [deviceName], contains all the [components] belonging to the logical device and
 * the [deploymentUnits] belonging to the logical device.
 */
data class LogicalDeviceConfiguration(
    val deviceName: String,
    val components: Set<PulverizedComponentType>,
    val deploymentUnits: Set<DeploymentUnit>,
)

/**
 * Models the relation between a [device] and [otherDevice].
 */
data class DeviceLink(val device: String, val otherDevice: String)

/**
 * Represents the configuration of the all available [links] between device.
 */
data class DeviceRelationsConfiguration(val links: Set<DeviceLink>)

/**
 * Represents the configuration of the pulverization system containing a [devicesConfig] and [devicesLinks].
 */
data class PulverizationConfiguration(
    val devicesConfig: Set<LogicalDeviceConfiguration>,
    val devicesLinks: DeviceRelationsConfiguration,
)

/**
 * Utility function to retrive a [LogicalDeviceConfiguration] given the device [name].
 * If the [name] do not match any device, null is returned.
 */
fun PulverizationConfiguration.getDeviceConfiguration(name: String): LogicalDeviceConfiguration? =
    devicesConfig.firstOrNull { it.deviceName == name }

/**
 * Utility function to retrive a [DeploymentUnit] from a [LogicalDeviceConfiguration].
 * To retrive the [DeploymentUnit], a complete set of [components] must be provided.
 * The [components] set must match 1:1 all the components in the deployment unit.
 */
fun LogicalDeviceConfiguration.getDeploymentUnit(components: Set<PulverizedComponentType>): DeploymentUnit? =
    deploymentUnits.firstOrNull {
        it.deployableComponents.containsAll(components) && it.deployableComponents.size == components.size
    }

/**
 * Utility function to retrive a [DeploymentUnit] from a [LogicalDeviceConfiguration].
 * To retrive the [DeploymentUnit], a complete set of [components] must be provided.
 * The [components] set must match 1:1 all the components in the deployment unit.
 */
fun LogicalDeviceConfiguration.getDeploymentUnit(vararg components: PulverizedComponentType): DeploymentUnit? =
    getDeploymentUnit(components.toSet())
