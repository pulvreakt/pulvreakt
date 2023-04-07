package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host

/**
 * Scope class for configuring the reconfiguration.
 */
class ReconfigurationComponentScope {
    private lateinit var reconfiguration: Pair<ComponentType, Host>

    /**
     * Configure where the [ComponentType] should me moved on which [host].
     */
    infix fun ComponentType.movesTo(host: Host) {
        reconfiguration = this to host
    }

    internal fun generate(): Pair<ComponentType, Host> {
        if (!::reconfiguration.isInitialized) error("Reconfiguration is not configured")
        return reconfiguration
    }
}
