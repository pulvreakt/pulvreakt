package it.unibo.pulvreakt.dsl.errors

import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.model.ComponentType

/**
 * Represents an error that can occur during the configuration of the system using the DSL.
 */
sealed interface ConfigurationError

/**
 * Represents an error that can occur during the configuration of the deployment using the DSL.
 */
sealed interface DeploymentConfigurationError : ConfigurationError {
    /**
     * Error raised if a device with the given [deviceName] is not found in the system configuration.
     */
    data class NoDeviceFound(val deviceName: String) : DeploymentConfigurationError

    /**
     * Error raised if the deployment configuration does not match the system configuration.
     */
    object MismatchWithSystemConfiguration : DeploymentConfigurationError

    /**
     * Error raised when in the system configuration a [component] is registered but in the deployment configuration
     * it is not provided its instance.
     */
    data class ComponentNotRegistered(val component: ComponentType) : DeploymentConfigurationError

    /**
     * Error raised when a [component] cannot be moved to the specified [host].
     */
    data class InvalidReconfiguration(val component: ComponentType, val host: Host) : DeploymentConfigurationError

    /**
     * Error raised when a [component] cannot be started on the specified [host].
     */
    data class InvalidStartupHost(val component: ComponentType, val host: Host) : DeploymentConfigurationError

    /**
     * Error raised when a [component] is configured for a reconfiguration, but it does not belong to the device.
     */
    data class UnknownComponent(val component: ComponentType) : DeploymentConfigurationError

    /**
     * Error raised when a component is moved to an [host] that is not in the infrastructure.
     */
    data class InvalidReconfigurationHost(val host: Host) : DeploymentConfigurationError

    /**
     * Error raised when the reconfiguration rules scope is empty.
     */
    object EmptyOnDeviceReconfiguration : DeploymentConfigurationError

    /**
     * Error raised when the deployment configuration is empty.
     */
    object EmptyDeploymentConfiguration : DeploymentConfigurationError
}

/**
 * Represents an error that can occur during the configuration of the system using the DSL.
 */
sealed interface SystemConfigurationError : ConfigurationError {
    /**
     * Error raised if two devices have the same [deviceName].
     */
    data class DuplicateDeviceName(val deviceName: String) : SystemConfigurationError

    /**
     * Error raised if the system configuration is empty.
     */
    object EmptySystemConfiguration : SystemConfigurationError

    /**
     * Error raised if the system configuration does not contain a device configuration.
     */
    object EmptyDeviceConfiguration : SystemConfigurationError

    /**
     * Error raised if a [component] is registered without specifying its capabilities.
     */
    data class UnspecifiedCapabilities(val component: ComponentType) : SystemConfigurationError
}
