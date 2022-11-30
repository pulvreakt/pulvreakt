package it.nicolasfarabegoli.pulverization.runtime.communication

import kotlinx.coroutines.flow.Flow

interface Communicator {

    suspend fun setup(binding: Binding)

    suspend fun fireMessage(message: ByteArray)

    fun receiveMessage(): Flow<ByteArray>
}
