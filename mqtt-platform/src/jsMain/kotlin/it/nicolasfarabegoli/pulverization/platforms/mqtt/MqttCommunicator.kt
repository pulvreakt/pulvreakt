package it.nicolasfarabegoli.pulverization.platforms.mqtt

import it.nicolasfarabegoli.pulverization.runtime.communication.Binding
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Implement the [Communicator] interface relying on MQTT as a platform for communications.
 */
actual class MqttCommunicator actual constructor(
    hostname: String,
    port: Int,
    username: String,
    password: String,
    dispatcher: CoroutineDispatcher,
) : Communicator {
    override suspend fun setup(binding: Binding, remotePlace: RemotePlace?) {
        TODO("Not yet implemented")
    }

    override suspend fun finalize() {
        TODO("Not yet implemented")
    }

    override suspend fun fireMessage(message: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun receiveMessage(): Flow<ByteArray> {
        TODO("Not yet implemented")
    }
}
