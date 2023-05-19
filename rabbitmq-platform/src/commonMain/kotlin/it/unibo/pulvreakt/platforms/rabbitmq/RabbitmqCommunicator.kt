package it.unibo.pulvreakt.platforms.rabbitmq

import it.unibo.pulvreakt.dsl.model.ComponentType
import it.unibo.pulvreakt.dsl.model.show
import it.unibo.pulvreakt.runtime.communication.Communicator
import it.unibo.pulvreakt.runtime.communication.RemotePlace
import it.unibo.pulvreakt.runtime.communication.RemotePlaceProvider
import it.unibo.pulvreakt.runtime.context.ExecutionContext
import it.unibo.pulvreakt.utils.PulverizationKoinModule
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
    override val context: ExecutionContext by inject()

    override fun get(type: ComponentType): RemotePlace {
        return RemotePlace(context.deviceID, type.show())
    }
}
