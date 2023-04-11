package it.nicolasfarabegoli.pulverization.runtime.context

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host

/**
 * Execution context of the system.
 * [host] represents the [Host] on which the deployment unit runs on.
 */
interface ExecutionContext : Context {
    /**
     * The host on which the system run on.
     */
    val host: Host
}
