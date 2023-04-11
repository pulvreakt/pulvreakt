package it.nicolasfarabegoli.pulverization.runtime.reconfiguration

import it.nicolasfarabegoli.pulverization.core.Initializable
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host
import kotlinx.coroutines.flow.Flow

/**
 * Represents how the reconfiguration of a deployment unit should occur, abstracting from the specific protocol.
 */
interface Reconfigurator : Initializable {
    /**
     * Method used for send a [newConfiguration] of a device.
     */
    suspend fun reconfigure(newConfiguration: Pair<ComponentType, Host>)

    /**
     * Async flow used to receive a new configuration of a device.
     */
    fun receiveReconfiguration(): Flow<Pair<ComponentType, Host>>
}
