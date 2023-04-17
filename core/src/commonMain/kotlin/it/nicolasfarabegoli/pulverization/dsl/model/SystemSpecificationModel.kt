package it.nicolasfarabegoli.pulverization.dsl.model

import it.nicolasfarabegoli.pulverization.core.PulverizedComponent
import kotlinx.serialization.Serializable

/**
 * Represents the capability on which a [PulverizedComponent] should runs on.
 */
interface Capability

/**
 * All available pulverization components type.
 */
@Serializable
sealed interface ComponentType

/**
 * Behaviour component.
 */
@Serializable
object Behaviour : ComponentType

/**
 * State component.
 */
@Serializable
object State : ComponentType

/**
 * Communication component.
 */
@Serializable
object Communication : ComponentType

/**
 * Actuators component.
 */
@Serializable
object Actuators : ComponentType

/**
 * Sensors component.
 */
@Serializable
object Sensors : ComponentType

/**
 * Utility extension method to return a string representation for each [PulverizedComponentType].
 */
fun ComponentType.show(): String =
    when (this) {
        Actuators -> "actuators"
        Behaviour -> "behaviour"
        Communication -> "communication"
        Sensors -> "sensors"
        State -> "state"
    }

/**
 * Configuration of a device called [deviceName] where its [components] have [requiredCapabilities] in oder to run.
 */
data class LogicalDeviceSpecification(
    val deviceName: String,
    val requiredCapabilities: Map<ComponentType, Set<Capability>>,
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
