package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import com.rabbitmq.client.Connection
import it.nicolasfarabegoli.pulverization.core.DeviceID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject
import reactor.core.publisher.Mono
import reactor.rabbitmq.BindingSpecification
import reactor.rabbitmq.OutboundMessage
import reactor.rabbitmq.RabbitFlux
import reactor.rabbitmq.Receiver
import reactor.rabbitmq.ReceiverOptions
import reactor.rabbitmq.Sender
import reactor.rabbitmq.SenderOptions

/**
 * Simple implementation for communicate (send only) with another component using RabbitMQ.
 */
actual class SimpleRabbitmqSenderCommunicator<in Send, I : DeviceID>(
    override val id: I,
    override val queue: String,
    private val serializer: SerializationStrategy<Send>,
) : RabbitmqSenderCommunicator<Send, I>, KoinComponent {
    private val connection: Connection by inject(Connection::class.java)
    private val sender: Sender

    companion object {
        private const val EXCHANGE = "pulverization.exchange"
    }

    init {
        val options = SenderOptions().connectionSupplier { connection }
        sender = RabbitFlux.createSender(options)
        sender.bindQueue(
            BindingSpecification().queue(queue).exchange(EXCHANGE).routingKey(id.toString()),
        ).block()
    }

    override suspend fun sendToComponent(payload: Send) {
        val message = OutboundMessage("pulverization.", "", Json.encodeToString(serializer, payload).toByteArray())
        sender.send(Mono.just(message)).awaitSingleOrNull()
    }
}

/**
 * Simple implementation for communicate (receive only) with another component using RabbitMQ.
 */
actual class SimpleRabbitmqReceiverCommunicator<out Receive, I : DeviceID>(
    override val id: I,
    override val queue: String,
    private val serializer: DeserializationStrategy<Receive>,
) : RabbitmqReceiverCommunicator<Receive, I>, KoinComponent {
    private val connection: Connection by inject(Connection::class.java)
    private val receiver: Receiver

    init {
        val options = ReceiverOptions().connectionSupplier { connection }
        receiver = RabbitFlux.createReceiver(options)
    }

    override fun receiveFromComponent(): Flow<Receive> {
        return receiver.consumeAutoAck(queue).asFlow().map { Json.decodeFromString(serializer, it.body.toString()) }
    }
}

/**
 * Simple implementation for communicate with another component using RabbitMQ.
 */
actual class SimpleRabbitmqBidirectionalCommunication<in Send, out Receive, I : DeviceID>(
    override val id: I,
    override val queue: String,
    private val serializer: SerializationStrategy<Send>,
    private val deserializer: DeserializationStrategy<Receive>,
) : RabbitmqBidirectionalCommunicator<Send, Receive, I>, KoinComponent {
    private val connection: Connection by inject(Connection::class.java)
    private val sender: Sender
    private val receiver: Receiver

    companion object {
        private const val EXCHANGE = "pulverization.exchange"
    }

    init {
        val senderOptions = SenderOptions().connectionSupplier { connection }
        val receiverOptions = ReceiverOptions().connectionSupplier { connection }
        sender = RabbitFlux.createSender(senderOptions)
        receiver = RabbitFlux.createReceiver(receiverOptions)
    }

    override suspend fun sendToComponent(payload: Send) {
        val message = OutboundMessage(
            EXCHANGE,
            id.toString(),
            Json.encodeToString(serializer, payload).toByteArray(),
        )
        sender.send(Mono.just(message)).awaitSingleOrNull()
    }

    override fun receiveFromComponent(): Flow<Receive> {
        return receiver.consumeAutoAck(queue).asFlow().map { Json.decodeFromString(deserializer, it.body.toString()) }
    }
}
