package it.unibo.pulvreakt.dsl.deployment.errors

import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.infrastructure.Host

sealed interface DeploymentDslError {
    data class NoDeviceFound(val deviceName: String) : DeploymentDslError
    data class ComponentNotRegistered(val component: ComponentType<*>) : DeploymentDslError
    data class InvalidReconfiguration(val component: ComponentType<*>, val host: Host) : DeploymentDslError
    data class UnknownComponent(val component: ComponentType<*>) : DeploymentDslError
    object MissingCommunicator : DeploymentDslError
    object MissingReconfigurator : DeploymentDslError
}
