package it.unibo.pulvreakt.mqtt

import it.unibo.pulvreakt.api.communication.protocol.Protocol
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Represents the MQTT protocol used in PulvReAKt.
 */
expect class MqttProtocol(
    host: String = "localhost",
    port: Int = 1883,
    username: String? = null,
    password: String? = null,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : Protocol
