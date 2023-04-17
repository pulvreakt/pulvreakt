package it.nicolasfarabegoli.pulverization.platforms.rabbitmq

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import it.nicolasfarabegoli.pulverization.dsl.model.ComponentType
import it.nicolasfarabegoli.pulverization.runtime.context.ExecutionContext
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.Host
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.Reconfigurator
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
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
            declareExchange(ExchangeSpecification.exchange(EXCHANGE).type("fanout").durable(false))
                .awaitSingleOrNull() ?: error("Failed to declare exchange `$EXCHANGE`")

            reconfigurationQueue = "reconfiguration/${executionContext.deviceID}"

            declareQueue(QueueSpecification.queue(reconfigurationQueue).durable(false))
                .awaitSingleOrNull() ?: error("Unable to create the queue `$reconfigurationQueue`")
            bindQueue(BindingSpecification().queue(reconfigurationQueue).exchange(EXCHANGE))
                .awaitSingleOrNull() ?: error("Unable to bind `$EXCHANGE` to `$reconfigurationQueue`")
        }
    }

    override suspend fun finalize() {
        sender.close()
        receiver.close()
    }

    override suspend fun reconfigure(newConfiguration: Pair<ComponentType, Host>) {
        val payload = Json.encodeToString(newConfiguration).encodeToByteArray()
        val message = OutboundMessage(EXCHANGE, "", payload)
        sender.send(Mono.just(message)).awaitSingleOrNull()
    }

    override fun receiveReconfiguration(): Flow<Pair<ComponentType, Host>> =
        receiver.consumeAutoAck(reconfigurationQueue)
            .map { Json.decodeFromString<Pair<ComponentType, Host>>(it.body.decodeToString()) }
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
