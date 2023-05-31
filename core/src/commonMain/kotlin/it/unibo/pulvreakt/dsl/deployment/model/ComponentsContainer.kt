package it.unibo.pulvreakt.dsl.deployment.model

import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.infrastructure.Host

/**
 * Represents the registered components and their startup hosts.
 */
typealias ComponentsContainer = Map<Component<*>, Host>
