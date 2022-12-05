package it.nicolasfarabegoli.pulverization.platforms.rabbitmq

import it.nicolasfarabegoli.pulverization.runtime.communication.Binding
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.coroutines.flow.Flow

/**
 * TODO.
 */
class RabbitmqCommunicator : Communicator {
    override suspend fun setup(binding: Binding) {
        TODO("Not yet implemented")
    }

    override suspend fun fireMessage(message: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun receiveMessage(): Flow<ByteArray> {
        TODO("Not yet implemented")
    }
}
