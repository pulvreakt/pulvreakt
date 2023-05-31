package it.unibo.pulvreakt.core.reconfiguration

import kotlinx.coroutines.flow.Flow

/**
 * Represents the ability of reconfiguring the deployment unit.
 */
interface Reconfigurator {
    /**
     * Reconfigures the deployment unit with the given [newConfiguration].
     */
    suspend fun reconfigure(newConfiguration: ReconfigurationMessage)

    /**
     * Returns the flow of incoming reconfiguration messages.
     */
    fun receiveConfiguration(): Flow<ReconfigurationMessage>
}
