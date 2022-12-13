package example.components

import com.rabbitmq.client.ConnectionFactory
import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Communication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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

actual class CommunicationComp : Communication<NeighboursMessage> {
    override val context: Context by inject()

    private lateinit var sender: Sender
    private lateinit var receiver: Receiver

    companion object {
        private const val RMQ_PORT = 5672
    }

    override suspend fun initialize() {
        val connectionFactory = ConnectionFactory()
        connectionFactory.useNio()
        connectionFactory.apply {
            host = "rabbitmq"
            port = RMQ_PORT
            username = "guest"
            password = "guest"
            virtualHost = "/"
        }
        val connection = connectionFactory.newConnection()
        val senderOption = SenderOptions().connectionSupplier { connection }
        val receiverOption = ReceiverOptions().connectionSupplier { connection }
        sender = RabbitFlux.createSender(senderOption)
        receiver = RabbitFlux.createReceiver(receiverOption)

        sender.apply {
            declareExchange(ExchangeSpecification.exchange("neighbours").type("fanout")).awaitSingleOrNull()
            declareQueue(QueueSpecification.queue("neighbours/comm").durable(false)).awaitSingleOrNull()
            bindQueue(BindingSpecification().exchange("neighbours").queue("neighbours/comm").routingKey(""))
                .awaitSingleOrNull()
        }
    }

    override suspend fun send(payload: NeighboursMessage) {
        val message = OutboundMessage("neighbours", "", Json.encodeToString(payload).toByteArray())
        sender.send(Mono.just(message)).awaitSingleOrNull()
    }

    override fun receive(): Flow<NeighboursMessage> =
        receiver.consumeAutoAck("neighbours/comm")
            .asFlow()
            .map { Json.decodeFromString<NeighboursMessage>(it.body.decodeToString()) }
            .filter { it.device != context.deviceID }

    override suspend fun finalize() {
        sender.close()
        receiver.close()
    }
}
