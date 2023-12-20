package it.unibo.pulvreakt.api.reconfiguration

import it.unibo.pulvreakt.api.initializable.InjectAwareResource
import it.unibo.pulvreakt.api.initializable.ManagedResource
import kotlinx.coroutines.flow.Flow

/**
 * Represents the ability of reconfiguring the deployment unit.
 */
interface Reconfigurator : ManagedResource<Nothing>, InjectAwareResource {
    /**
     * Reconfigures the deployment unit with the given [newConfiguration].
     */
    suspend fun reconfigure(newConfiguration: ReconfigurationMessage)

    /**
     * Returns the flow of incoming reconfiguration messages.
     */
    fun receiveConfiguration(): Flow<ReconfigurationMessage>

    companion object {
        /**
         * Smart constructor for [Reconfigurator].
         */
        operator fun invoke(): Reconfigurator = ReconfiguratorImpl()
    }
}
