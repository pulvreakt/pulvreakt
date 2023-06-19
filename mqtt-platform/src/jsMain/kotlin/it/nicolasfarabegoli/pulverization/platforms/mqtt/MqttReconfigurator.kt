package it.nicolasfarabegoli.pulverization.platforms.mqtt

import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.NewConfiguration
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.Reconfigurator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Implement the [Reconfigurator] interface relying on Mqtt as a platform for communication.
 */
actual class MqttReconfigurator actual constructor(
    hostname: String,
    port: Int,
    username: String,
    password: String,
    dispatcher: CoroutineDispatcher,
) : Reconfigurator {
    override suspend fun reconfigure(newConfiguration: NewConfiguration) {
        TODO("Not yet implemented")
    }

    override fun receiveReconfiguration(): Flow<NewConfiguration> {
        TODO("Not yet implemented")
    }
}
