package it.unibo.pulvreakt.dsl.errors

import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.infrastructure.Host

/**
 * Represents an error that can occur during the configuration of the system using the DSL.
 */
sealed interface ConfigurationError

/**
 * Represents an error that can occur during the configuration of the deployment using the DSL.
 */
sealed interface DeploymentConfigurationError : ConfigurationError

/**
 * Error raised if a device with the given [deviceName] is not found in the system configuration.
 */
data class NoDeviceFound(val deviceName: String) : DeploymentConfigurationError

/**
 * Error raised when in the system configuration a [component] belonging to a [deviceType] is registered but in the deployment configuration
 * it is not provided its instance.
 */
data class ComponentNotRegistered(val deviceType: String, val component: ComponentRef) : DeploymentConfigurationError

/**
 * Error raised when a [component] cannot be moved to the specified [host].
 */
data class InvalidReconfiguration(val component: ComponentRef, val host: Host) : DeploymentConfigurationError

/**
 * Error raised when a [component] cannot be started on the specified [host].
 */
data class InvalidStartupHost(val component: ComponentRef, val host: Host) : DeploymentConfigurationError

/**
 * Error raised when a [host] is not in the infrastructure.
 */
data class UnknownHost(val host: Host) : DeploymentConfigurationError

/**
 * Error raised when a [component] is configured for a reconfiguration, but it does not belong to the device.
 */
data class UnknownComponent(val component: ComponentRef) : DeploymentConfigurationError

/**
 * Error raised when a component is moved to an [host] that is not in the infrastructure.
 */
data class InvalidReconfigurationHost(val host: Host) : DeploymentConfigurationError

/**
 * Error raised when the deployment configuration is empty.
 */
data object EmptyDeploymentConfiguration : DeploymentConfigurationError

/**
 * Represents an error that can occur during the configuration of the system using the DSL.
 */
sealed interface SystemConfigurationError : ConfigurationError

/**
 * Error raised if two devices have the same [deviceName].
 */
data class DuplicateDeviceName(val deviceName: String) : SystemConfigurationError

/**
 * Error raised if the system configuration is empty.
 */
data object EmptySystemConfiguration : SystemConfigurationError

/**
 * Error raised if the system configuration does not contain a device configuration.
 */
data object EmptyDeviceConfiguration : SystemConfigurationError

/**
 * Error raised if a [component] is registered without specifying its capabilities.
 */
data class UnspecifiedCapabilities(val component: ComponentRef) : SystemConfigurationError
