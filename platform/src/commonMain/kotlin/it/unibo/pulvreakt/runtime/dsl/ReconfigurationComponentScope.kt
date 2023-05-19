package it.unibo.pulvreakt.runtime.dsl

import it.unibo.pulvreakt.dsl.model.ComponentType
import it.unibo.pulvreakt.runtime.dsl.model.Host
import it.unibo.pulvreakt.runtime.reconfiguration.NewConfiguration

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
