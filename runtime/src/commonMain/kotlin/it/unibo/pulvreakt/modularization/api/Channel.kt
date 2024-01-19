package it.unibo.pulvreakt.modularization.api

import kotlinx.coroutines.flow.Flow

interface Channel<Message : Any, Address : Any> {
    fun setup(from: Address, to: Address)
    suspend fun send(message: Message)
    fun receive(): Flow<Message>
}
