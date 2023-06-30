package it.unibo.pulvreakt.dsl.errors

import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.model.ComponentType

sealed interface ConfigurationError

sealed interface DeploymentConfigurationError : ConfigurationError {
    data class NoDeviceFound(val deviceName: String) : DeploymentConfigurationError
    object MismatchWithSystemConfiguration : DeploymentConfigurationError
    data class ComponentNotRegistered(val component: ComponentType) : DeploymentConfigurationError
    data class InvalidReconfiguration(val component: ComponentType, val host: Host) : DeploymentConfigurationError
    data class InvalidStartupHost(val component: ComponentType, val host: Host) : DeploymentConfigurationError
    data class InvalidReconfigurationComponent(val component: ComponentType) : DeploymentConfigurationError
    data class InvalidReconfigurationHost(val host: Host) : DeploymentConfigurationError
    object EmptyOnDeviceReconfiguration : DeploymentConfigurationError
    data class UnknownComponent(val component: ComponentType) : DeploymentConfigurationError
    object MissingCommunicator : DeploymentConfigurationError
    object MissingReconfigurator : DeploymentConfigurationError
    object EmptyDeploymentConfiguration : DeploymentConfigurationError
}

sealed interface SystemConfigurationError : ConfigurationError {
    data class DuplicateDeviceName(val deviceName: String) : SystemConfigurationError
    object EmptySystemConfiguration : SystemConfigurationError
    object EmptyDeviceConfiguration : SystemConfigurationError
    data class UnspecifiedCapabilities(val component: ComponentType) : SystemConfigurationError
}
