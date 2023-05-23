package it.unibo.pulvreakt.core.reconfiguration

import kotlinx.coroutines.flow.Flow

interface Reconfigurator {
    suspend fun reconfigure(newConfiguration: ReconfigurationMessage)
    fun receiveConfiguration(): Flow<ReconfigurationMessage>
}
