package it.unibo.pulvreakt.platforms.rabbitmq

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import it.unibo.pulvreakt.runtime.context.ExecutionContext
import it.unibo.pulvreakt.runtime.reconfiguration.NewConfiguration
import it.unibo.pulvreakt.runtime.reconfiguration.Reconfigurator
import it.unibo.pulvreakt.utils.PulverizationKoinModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import reactor.core.publisher.Mono
import reactor.rabbitmq.BindingSpecification
import reactor.rabbitmq.ExchangeSpecification
import reactor.rabbitmq.OutboundMessage
import reactor.rabbitmq.QueueSpecification
import reactor.rabbitmq.RabbitFlux
import reactor.rabbitmq.Receiver
import reactor.rabbitmq.ReceiverOptions
import reactor.rabbitmq.Sender
import reactor.rabbitmq.SenderOptions

/**
 * Implement the [Reconfigurator] interface relying on RabbitMQ as a platform for communication.
 */
actual class RabbitmqReconfigurator actual constructor(
    private val hostname: String,
    private val port: Int,
    private val username: String,
    private val password: String,
    private val virtualHost: String,
) : Reconfigurator, KoinComponent {
    private lateinit var sender: Sender
    private lateinit var receiver: Receiver
    private lateinit var reconfigurationQueue: String
    private lateinit var routingKey: String
    private val executionContext: ExecutionContext by inject()

    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("Koin module not initialized")

    companion object {
        private const val EXCHANGE = "pulverization.reconfiguration"
    }

    override suspend fun initialize() {
        val connection = initConnection()
        with(connection) {
            val senderOptions = SenderOptions().connectionSupplier { this }
            val receiverOptions = ReceiverOptions().connectionSupplier { this }
            sender = RabbitFlux.createSender(senderOptions)
            receiver = RabbitFlux.createReceiver(receiverOptions)
        }
        with(sender) {
            declareExchange(ExchangeSpecification.exchange(EXCHANGE).type("topic").durable(false))
                .awaitSingleOrNull() ?: error("Failed to declare exchange `$EXCHANGE`")

            reconfigurationQueue = "reconfiguration/${executionContext.host.hostname}/${executionContext.deviceID}"
            routingKey = "reconfiguration.${executionContext.deviceID}"

            declareQueue(QueueSpecification.queue(reconfigurationQueue).durable(false))
                .awaitSingleOrNull() ?: error("Unable to create the queue `$reconfigurationQueue`")
            bindQueue(BindingSpecification().queue(reconfigurationQueue).exchange(EXCHANGE).routingKey(routingKey))
                .awaitSingleOrNull() ?: error("Unable to bind `$EXCHANGE` to `$reconfigurationQueue` with $routingKey")
        }
    }

    override suspend fun finalize() {
        sender.close()
        receiver.close()
    }

    override suspend fun reconfigure(newConfiguration: NewConfiguration) {
        val payload = Json.encodeToString(newConfiguration).encodeToByteArray()
        val message = OutboundMessage(EXCHANGE, routingKey, payload)
        sender.send(Mono.just(message)).awaitSingleOrNull()
    }

    override fun receiveReconfiguration(): Flow<NewConfiguration> =
        receiver.consumeAutoAck(reconfigurationQueue)
            .map { Json.decodeFromString<NewConfiguration>(it.body.decodeToString()) }
            .asFlow()

    private fun initConnection(): Connection {
        val connectionFactory = ConnectionFactory()
        connectionFactory.useNio()
        connectionFactory.apply {
            host = hostname
            port = this@RabbitmqReconfigurator.port
            username = this@RabbitmqReconfigurator.username
            password = this@RabbitmqReconfigurator.password
            virtualHost = this@RabbitmqReconfigurator.virtualHost
        }
        return connectionFactory.newConnection()
    }
}
