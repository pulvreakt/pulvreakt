package it.nicolasfarabegoli.pulverization.platforms.rabbitmq

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.core.show
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Implement the [Communicator] interface relying on RabbitMQ as a platform for communications.
 */
expect class RabbitmqCommunicator(
    hostname: String = "localhost",
    port: Int = 5672,
    username: String = "guest",
    password: String = "guest",
    virtualHost: String = "/",
) : Communicator

/**
 * Default representation for a [RemotePlaceProvider] used by the [RabbitmqCommunicator].
 */
fun defaultRabbitMQRemotePlace(): RemotePlaceProvider = object : RemotePlaceProvider, KoinComponent {
    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("No Koin app defined")
    override val context: Context by inject()

    override fun get(type: PulverizedComponentType): RemotePlace {
        return RemotePlace(context.deviceID, type.show())
    }
}
