package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.deployment.model.NewConfiguration

/**
 * Scope for the reconfiguration of a component.
 */
class ReconfigurationComponentScope {
    private lateinit var reconfiguration: NewConfiguration

    /**
     * The component moves to the [host].
     */
    infix fun ComponentType<*>.movesTo(host: Host) {
        reconfiguration = this to host
    }

    internal fun generate(): Either<String, NewConfiguration> = either {
        ensure(::reconfiguration.isInitialized) { "No reconfiguration is registered" }
        reconfiguration
    }
}
