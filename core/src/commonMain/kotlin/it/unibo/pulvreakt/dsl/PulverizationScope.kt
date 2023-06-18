package it.unibo.pulvreakt.dsl

import arrow.core.NonEmptySet
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.deployment.DeploymentSpecificationScope
import it.unibo.pulvreakt.dsl.system.SystemSpecificationScope

class PulverizationScope {
    fun system(systemConfig: SystemSpecificationScope.() -> Unit): Nothing = TODO()
    fun deployment(infrastructure: NonEmptySet<Host>, deploymentConfig: DeploymentSpecificationScope.() -> Unit): Nothing = TODO()

    internal fun generate(): Nothing = TODO()
}
