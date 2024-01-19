package it.unibo.pulvreakt.modularization.api.deployment.network

import kotlinx.coroutines.flow.Flow

interface Network {
    suspend fun send(message: NetworkMessage)
    fun receive(): Flow<NetworkMessage>
}
