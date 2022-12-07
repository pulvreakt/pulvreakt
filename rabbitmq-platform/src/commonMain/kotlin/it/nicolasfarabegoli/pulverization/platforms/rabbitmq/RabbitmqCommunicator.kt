package it.nicolasfarabegoli.pulverization.platforms.rabbitmq

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.core.show
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * TODO.
 */
expect class RabbitmqCommunicator(
    hostname: String = "localhost",
    port: Int = 5672,
    username: String = "guest",
    password: String = "guest",
    virtualHost: String = "/",
) : Communicator

/**
 * TODO.
 */
fun defaultRabbitMQRemotePlace(): RemotePlaceProvider = object : RemotePlaceProvider, KoinComponent {
    override val context: Context by inject()

    override fun get(type: PulverizedComponentType): RemotePlace {
        return RemotePlace(context.deviceID, type.show())
    }
}
