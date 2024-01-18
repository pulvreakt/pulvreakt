package it.unibo.pulvreakt.modularization.api

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
data class DiscoveredHost(val host: String, val capabilities: Capabilities)

interface HostDiscover {

    suspend fun setup()
    fun discover(): Flow<DiscoveredHost>
}
