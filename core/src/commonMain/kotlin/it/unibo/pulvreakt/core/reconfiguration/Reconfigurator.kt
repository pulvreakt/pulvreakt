package it.unibo.pulvreakt.core.reconfiguration

import it.unibo.pulvreakt.core.utils.Initializable
import it.unibo.pulvreakt.core.utils.PulvreaktInjected
import kotlinx.coroutines.flow.Flow

/**
 * Represents the ability of reconfiguring the deployment unit.
 */
interface Reconfigurator : Initializable<Nothing>, PulvreaktInjected {
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
