package it.unibo.pulvreakt.platforms.rabbitmq

import it.unibo.pulvreakt.runtime.reconfiguration.NewConfiguration
import it.unibo.pulvreakt.runtime.reconfiguration.Reconfigurator
import kotlinx.coroutines.flow.Flow

/**
 * Implement the [Reconfigurator] interface relying on RabbitMQ as a platform for communication.
 */
actual class RabbitmqReconfigurator actual constructor(
    hostname: String,
    port: Int,
    username: String,
    password: String,
    virtualHost: String,
) : Reconfigurator {
    override suspend fun reconfigure(newConfiguration: NewConfiguration) {
        TODO("Not yet implemented")
    }

    override fun receiveReconfiguration(): Flow<NewConfiguration> {
        TODO("Not yet implemented")
    }
}
