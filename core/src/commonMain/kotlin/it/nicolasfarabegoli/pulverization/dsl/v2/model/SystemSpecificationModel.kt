package it.nicolasfarabegoli.pulverization.dsl.v2.model

import it.nicolasfarabegoli.pulverization.core.PulverizedComponent

/**
 * Represents the capability on which a [PulverizedComponent].
 */
interface Capability

/**
 * All available pulverization components type.
 */
sealed interface ComponentType
object Behaviour : ComponentType
object State : ComponentType
object Communication : ComponentType
object Actuators : ComponentType
object Sensors : ComponentType

/**
 * Configuration of a device called [deviceName] with its [capabilities] and its [components].
 */
data class LogicalDeviceSpecification(
    val deviceName: String,
    val capabilities: Map<ComponentType, Set<Capability>>,
    val components: Set<ComponentType>,
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
