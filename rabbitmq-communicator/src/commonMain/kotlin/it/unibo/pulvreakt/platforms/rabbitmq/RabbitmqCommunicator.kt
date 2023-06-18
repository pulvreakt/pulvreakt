package it.unibo.pulvreakt.platforms.rabbitmq

import it.unibo.pulvreakt.core.communicator.AbstractCommunicator
import it.unibo.pulvreakt.core.communicator.Communicator

/**
 * Implement the [Communicator] interface relying on RabbitMQ as a platform for communications.
 */
expect class RabbitmqCommunicator(
    hostname: String = "localhost",
    port: Int = 5672,
    username: String = "guest",
    password: String = "guest",
    virtualHost: String = "/",
) : AbstractCommunicator

// /**
// * Default representation for a [RemotePlaceProvider] used by the [RabbitmqCommunicator].
// */
// fun defaultRabbitMQRemotePlace(): RemotePlaceProvider = object : RemotePlaceProvider, KoinComponent {
//    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("No Koin app defined")
//    override val context: ExecutionContext by inject()
//
//    override fun get(type: ComponentType): RemotePlace {
//        return RemotePlace(context.deviceID, type.show())
//    }
// }
