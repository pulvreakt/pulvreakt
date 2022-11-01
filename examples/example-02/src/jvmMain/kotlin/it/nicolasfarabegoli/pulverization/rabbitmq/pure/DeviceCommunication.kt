package it.nicolasfarabegoli.pulverization.rabbitmq.pure

import com.rabbitmq.client.Connection
import com.rabbitmq.client.Delivery
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.inject
import reactor.core.publisher.Mono
import reactor.rabbitmq.BindingSpecification
import reactor.rabbitmq.OutboundMessage
import reactor.rabbitmq.QueueSpecification
import reactor.rabbitmq.RabbitFlux
import reactor.rabbitmq.Receiver
import reactor.rabbitmq.ReceiverOptions
import reactor.rabbitmq.Sender
import reactor.rabbitmq.SenderOptions

class DeviceCommunication : Communication<CommPayload> {
    override val context: RabbitmqContext by inject()

    private val connection: Connection by inject()
    private val sender: Sender
    private val receiver: Receiver

    init {
        val senderOption = SenderOptions().connectionSupplier { connection }
        val receiverOption = ReceiverOptions().connectionSupplier { connection }
        sender = RabbitFlux.createSender(senderOption)
        receiver = RabbitFlux.createReceiver(receiverOption)
    }

    suspend fun initialize() {
        withContext(Dispatchers.IO) {
            sender.declare(QueueSpecification.queue("communication/${context.id.show()}"))
                .then(sender.bind(BindingSpecification.binding("amq.fanout", "", "communication/${context.id.show()}")))
                .block()
        } ?: error("Unable to create the exchange")
    }

    override fun send(payload: CommPayload) {
        println("${DeviceCommunication::class.simpleName}: broadcast $payload")
        val message = OutboundMessage("amq.fanout", "", Json.encodeToString(payload).toByteArray())
        sender.send(Mono.just(message)).block()
    }

    override fun receive(): Flow<CommPayload> {
        return receiver.consumeAutoAck("communication/${context.id.show()}").asFlow().map<Delivery, CommPayload> {
            Json.decodeFromString(it.body.decodeToString())
        }.filter { it.deviceID != context.id.show() }
    }
}
