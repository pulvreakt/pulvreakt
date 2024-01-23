package it.unibo.pulvreakt.modularization.runtime.api.scheduler

import it.unibo.pulvreakt.modularization.api.module.SymbolicModule
import it.unibo.pulvreakt.modularization.api.utils.ManagedResource
import it.unibo.pulvreakt.modularization.runtime.api.network.protocol.InboundMessage
import kotlinx.coroutines.flow.Flow

/**
 * Responsible to define the scheduling policy at which the modules are executed.
 * Given the messages received from the network, the scheduler returns the modules that should be executed and the associated messages to process.
 */
interface Scheduler : ManagedResource<Nothing> {
    /**
     * Registers the [messages] received from the network.
     */
    fun registerNewMessages(messages: List<InboundMessage>)

    /**
     * Returns an asynchronous flow of the modules that should be executed and the associated messages to process.
     */
    fun scheduledModulesFlow(): Flow<Map<SymbolicModule, *>>
}
