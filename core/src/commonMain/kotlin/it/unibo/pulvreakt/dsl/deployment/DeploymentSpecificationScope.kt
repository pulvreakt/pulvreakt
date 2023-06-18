package it.unibo.pulvreakt.dsl.deployment

import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.infrastructure.Host

/**
 * Scope for the deployment DSL configuration.
 */
class DeploymentSpecificationScope {
    infix fun Component.startsOn(host: Host): Nothing = TODO()

    fun reconfigurationRules(config: ReconfigurationRulesScope.() -> Unit): Nothing = TODO()
}
