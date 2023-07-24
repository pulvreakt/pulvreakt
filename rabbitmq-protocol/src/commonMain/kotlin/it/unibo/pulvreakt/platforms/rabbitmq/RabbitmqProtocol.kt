package it.unibo.pulvreakt.platforms.rabbitmq

import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.protocol.Protocol

/**
 * Implement the [Communicator] interface relying on RabbitMQ as a platform for communications.
 */
expect class RabbitmqProtocol(
    hostname: String = "localhost",
    port: Int = 5672,
    username: String = "guest",
    password: String = "guest",
    virtualHost: String = "/",
) : Protocol

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
