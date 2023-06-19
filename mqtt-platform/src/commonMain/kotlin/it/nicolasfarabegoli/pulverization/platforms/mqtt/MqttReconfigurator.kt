package it.nicolasfarabegoli.pulverization.platforms.mqtt

import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.Reconfigurator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Implement the [Reconfigurator] interface relying on Mqtt as a platform for communication.
 */
expect class MqttReconfigurator(
    hostname: String = "localhost",
    port: Int = 1883,
    username: String = "guest",
    password: String = "guest",
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : Reconfigurator
