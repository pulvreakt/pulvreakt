package it.unibo.pulvreakt.platforms.rabbitmq

import it.unibo.pulvreakt.core.reconfiguration.ReconfigurationMessage
import it.unibo.pulvreakt.core.reconfiguration.Reconfigurator
import kotlinx.coroutines.flow.Flow

/**
 * Implement the [Reconfigurator] interface relying on RabbitMQ as a platform for communication.
 */
actual class RabbitmqReconfigurator actual constructor(
    private val hostname: String,
    private val port: Int,
    private val username: String,
    private val password: String,
    private val virtualHost: String,
) : Reconfigurator {
    //    private lateinit var sender: Sender
//    private lateinit var receiver: Receiver
//    private lateinit var reconfigurationQueue: String
//    private lateinit var routingKey: String
//    private val executionContext: ExecutionContext by inject()
//
//    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("Koin module not initialized")
//
//    companion object {
//        private const val EXCHANGE = "pulverization.reconfiguration"
//    }
//
//    override suspend fun initialize() {
//        val connection = initConnection()
//        with(connection) {
//            val senderOptions = SenderOptions().connectionSupplier { this }
//            val receiverOptions = ReceiverOptions().connectionSupplier { this }
//            sender = RabbitFlux.createSender(senderOptions)
//            receiver = RabbitFlux.createReceiver(receiverOptions)
//        }
//        with(sender) {
//            declareExchange(ExchangeSpecification.exchange(EXCHANGE).type("topic").durable(false))
//                .awaitSingleOrNull() ?: error("Failed to declare exchange `$EXCHANGE`")
//
//            reconfigurationQueue = "reconfiguration/${executionContext.host.hostname}/${executionContext.deviceID}"
//            routingKey = "reconfiguration.${executionContext.deviceID}"
//
//            declareQueue(QueueSpecification.queue(reconfigurationQueue).durable(false))
//                .awaitSingleOrNull() ?: error("Unable to create the queue `$reconfigurationQueue`")
//            bindQueue(BindingSpecification().queue(reconfigurationQueue).exchange(EXCHANGE).routingKey(routingKey))
//                .awaitSingleOrNull() ?: error("Unable to bind `$EXCHANGE` to `$reconfigurationQueue` with $routingKey")
//        }
//    }
//
//    override suspend fun finalize() {
//        sender.close()
//        receiver.close()
//    }
//
//    override suspend fun reconfigure(newConfiguration: NewConfiguration) {
//        val payload = Json.encodeToString(newConfiguration).encodeToByteArray()
//        val message = OutboundMessage(EXCHANGE, routingKey, payload)
//        sender.send(Mono.just(message)).awaitSingleOrNull()
//    }
//
//    override fun receiveReconfiguration(): Flow<NewConfiguration> =
//        receiver.consumeAutoAck(reconfigurationQueue)
//            .map { Json.decodeFromString<NewConfiguration>(it.body.decodeToString()) }
//            .asFlow()
//
//    private fun initConnection(): Connection {
//        val connectionFactory = ConnectionFactory()
//        connectionFactory.useNio()
//        connectionFactory.apply {
//            host = hostname
//            port = this@RabbitmqReconfigurator.port
//            username = this@RabbitmqReconfigurator.username
//            password = this@RabbitmqReconfigurator.password
//            virtualHost = this@RabbitmqReconfigurator.virtualHost
//        }
//        return connectionFactory.newConnection()
//    }
    override suspend fun reconfigure(newConfiguration: ReconfigurationMessage) {
        TODO("Not yet implemented")
    }

    override fun receiveConfiguration(): Flow<ReconfigurationMessage> {
        TODO("Not yet implemented")
    }
}
