package it.unibo.pulvreakt.dsl.deployment.model

import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.infrastructure.Host

typealias NewConfiguration = Pair<ComponentType<*>, Host>
