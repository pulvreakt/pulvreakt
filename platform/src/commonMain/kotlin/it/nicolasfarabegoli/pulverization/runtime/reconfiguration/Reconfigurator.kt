package it.nicolasfarabegoli.pulverization.runtime.reconfiguration

import it.nicolasfarabegoli.pulverization.core.Initializable
import it.nicolasfarabegoli.pulverization.dsl.model.ComponentType
import kotlinx.coroutines.flow.Flow

typealias NewConfiguration = Pair<ComponentType, String>

/**
 * Represents how the reconfiguration of a deployment unit should occur, abstracting from the specific protocol.
 */
interface Reconfigurator : Initializable {
    /**
     * Method used for send a [newConfiguration] of a device.
     */
    suspend fun reconfigure(newConfiguration: NewConfiguration)

    /**
     * Async flow used to receive a new configuration of a device.
     */
    fun receiveReconfiguration(): Flow<NewConfiguration>
}
