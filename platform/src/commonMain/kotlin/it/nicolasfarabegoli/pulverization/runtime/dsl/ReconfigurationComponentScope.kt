package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.dsl.model.ComponentType
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.Host
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.NewConfiguration

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

    internal fun generate(): NewConfiguration {
        if (!::reconfiguration.isInitialized) error("Reconfiguration is not configured")
        return reconfiguration.first to reconfiguration.second.hostname
    }
}
