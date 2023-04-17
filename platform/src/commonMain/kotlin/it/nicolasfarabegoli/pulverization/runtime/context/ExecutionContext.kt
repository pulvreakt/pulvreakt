package it.nicolasfarabegoli.pulverization.runtime.context

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.Host

/**
 * Execution context of the system.
 * [host] represents the [Host] on which the deployment unit runs on.
 */
interface ExecutionContext : Context {
    /**
     * The host on which the system run on.
     */
    val host: Host

    companion object {
        /**
         * Creates a new execution context.
         */
        fun create(deviceID: String, host: Host): ExecutionContext = ExecutionContextImpl(deviceID, host)
    }
}

private class ExecutionContextImpl(override val deviceID: String, override val host: Host) : ExecutionContext
