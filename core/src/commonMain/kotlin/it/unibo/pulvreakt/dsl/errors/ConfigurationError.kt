package it.unibo.pulvreakt.dsl.errors

import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.model.ComponentName

sealed interface ConfigurationError

sealed interface DeploymentConfigurationError : ConfigurationError {
    data class NoDeviceFound(val deviceName: String) : DeploymentConfigurationError
    object MismatchWithSystemConfiguration : DeploymentConfigurationError
    data class ComponentNotRegistered(val component: ComponentName) : DeploymentConfigurationError
    data class InvalidReconfiguration(val component: ComponentName, val host: Host) : DeploymentConfigurationError
    data class InvalidStartupHost(val component: ComponentName, val host: Host) : DeploymentConfigurationError
    object EmptyOnDeviceReconfiguration : DeploymentConfigurationError
    data class UnknownComponent(val component: ComponentName) : DeploymentConfigurationError
    object MissingCommunicator : DeploymentConfigurationError
    object MissingReconfigurator : DeploymentConfigurationError
    object EmptyDeploymentConfiguration : DeploymentConfigurationError
}

sealed interface SystemConfigurationError : ConfigurationError {
    data class DuplicateDeviceName(val deviceName: String) : SystemConfigurationError
    object EmptySystemConfiguration : SystemConfigurationError
    object EmptyDeviceConfiguration : SystemConfigurationError
    data class UnspecifiedCapabilities(val component: ComponentName) : SystemConfigurationError
}
