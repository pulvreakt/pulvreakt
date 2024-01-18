package it.unibo.pulvreakt.modularization.api

import kotlinx.coroutines.flow.Flow

interface Channel<Message : Any> {
    suspend fun send(message: Message)
    fun receive(): Flow<Message>
}
