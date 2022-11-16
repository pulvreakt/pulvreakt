package it.nicolasfarabegoli.pulverization.dsl

import it.nicolasfarabegoli.pulverization.core.PulverizedComponent
import kotlin.reflect.KClass

sealed interface Tier
object Cloud : Tier
object Edge : Tier
object Device : Tier

data class DeploymentUnit(val deployableComponents: Set<KClass<out PulverizedComponent>>, val where: Tier)

data class LogicalDeviceConfiguration(
    val deviceName: String,
    val components: Set<KClass<out PulverizedComponent>>,
    val deploymentUnits: Set<DeploymentUnit>,
)

data class DeviceLink(val device: String, val otherDevice: String)

data class DeviceRelationsConfiguration(
    val links: Set<DeviceLink>,
)

data class PulverizationConfiguration(
    val devicesConfig: Set<LogicalDeviceConfiguration>,
    val devicesLinks: DeviceRelationsConfiguration,
)

fun PulverizationConfiguration.getDeviceConfiguration(name: String): LogicalDeviceConfiguration? =
    devicesConfig.firstOrNull { it.deviceName == name }

inline fun <reified C : PulverizedComponent> LogicalDeviceConfiguration.getDeploymentUnit(): DeploymentUnit? =
    deploymentUnits.firstOrNull { it.deployableComponents.contains(C::class) }
