package it.nicolasfarabegoli.pulverization.platforms.rabbitmq

import it.nicolasfarabegoli.pulverization.dsl.model.ComponentType
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.Host
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.Reconfigurator
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
    override suspend fun reconfigure(newConfiguration: Pair<ComponentType, Host>) {
        TODO("Not yet implemented")
    }

    override fun receiveReconfiguration(): Flow<Pair<ComponentType, Host>> {
        TODO("Not yet implemented")
    }
}
