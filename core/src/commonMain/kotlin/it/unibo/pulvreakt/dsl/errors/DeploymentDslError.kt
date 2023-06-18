package it.unibo.pulvreakt.dsl.errors

import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.model.ComponentName

sealed interface DeploymentDslError {
    data class NoDeviceFound(val deviceName: String) : DeploymentDslError
    data class ComponentNotRegistered(val component: ComponentName) : DeploymentDslError
    data class InvalidReconfiguration(val component: ComponentName, val host: Host) : DeploymentDslError
    data class UnknownComponent(val component: ComponentName) : DeploymentDslError
    object MissingCommunicator : DeploymentDslError
    object MissingReconfigurator : DeploymentDslError
}
