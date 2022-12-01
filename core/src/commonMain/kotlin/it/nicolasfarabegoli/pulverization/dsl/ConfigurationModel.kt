package it.nicolasfarabegoli.pulverization.dsl

import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType

sealed interface Tier
object Cloud : Tier
object Edge : Tier
object Device : Tier

data class DeploymentUnit(val deployableComponents: Set<PulverizedComponentType>, val where: Tier)

data class LogicalDeviceConfiguration(
    val deviceName: String,
    val components: Set<PulverizedComponentType>,
    val deploymentUnits: Set<DeploymentUnit>,
)

data class DeviceLink(val device: String, val otherDevice: String)

data class DeviceRelationsConfiguration(val links: Set<DeviceLink>)

data class PulverizationConfiguration(
    val devicesConfig: Set<LogicalDeviceConfiguration>,
    val devicesLinks: DeviceRelationsConfiguration,
)

fun PulverizationConfiguration.getDeviceConfiguration(name: String): LogicalDeviceConfiguration? =
    devicesConfig.firstOrNull { it.deviceName == name }

fun LogicalDeviceConfiguration.getDeploymentUnit(components: Set<PulverizedComponentType>): DeploymentUnit? =
    deploymentUnits.firstOrNull {
        it.deployableComponents.containsAll(components) && it.deployableComponents.size == components.size
    }

fun LogicalDeviceConfiguration.getDeploymentUnit(vararg components: PulverizedComponentType): DeploymentUnit? =
    getDeploymentUnit(components.toSet())
