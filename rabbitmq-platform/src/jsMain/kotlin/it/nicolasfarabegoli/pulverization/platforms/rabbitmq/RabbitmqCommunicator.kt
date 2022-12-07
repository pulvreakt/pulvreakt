package it.nicolasfarabegoli.pulverization.platforms.rabbitmq

import it.nicolasfarabegoli.pulverization.runtime.communication.Binding
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import kotlinx.coroutines.flow.Flow

/**
 * TODO.
 */
actual class RabbitmqCommunicator actual constructor(
    private val hostname: String,
    private val port: Int,
    private val username: String,
    private val password: String,
    private val virtualHost: String,
) : Communicator {
    override suspend fun setup(binding: Binding, remotePlace: RemotePlace?) {
        TODO("Not yet implemented")
    }

    override suspend fun fireMessage(message: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun receiveMessage(): Flow<ByteArray> {
        TODO("Not yet implemented")
    }
}
