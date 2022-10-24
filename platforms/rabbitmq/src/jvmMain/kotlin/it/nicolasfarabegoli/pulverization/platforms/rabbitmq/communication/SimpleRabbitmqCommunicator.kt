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
import kotlinx.serialization.serializer
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
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

/**
 * Simple implementation for communicate (send only) with another component using RabbitMQ.
 */
actual class SimpleRabbitmqSenderCommunicator<Send : Any, I : DeviceID>(
    private val type: KClass<Send>,
    override val id: I,
    override val queue: String,
) : RabbitmqSenderCommunicator<Send, I>, KoinComponent {
    private val connection: Connection by inject(Connection::class.java)
    private val sender: Sender
    private val serializer = Json.serializersModule.serializer(type.createType())

    companion object {
        /**
         * Creates the communication without the need of passing the KClass.
         */
        inline operator fun <reified S : Any> invoke(id: DeviceID, queue: String) =
            SimpleRabbitmqSenderCommunicator(S::class, id, queue)

        private const val EXCHANGE = "pulverization.exchange"
    }

    init {
        val options = SenderOptions().connectionSupplier { connection }
        sender = RabbitFlux.createSender(options)
    }

    override suspend fun initialize() {
        sender.bindQueue(
            BindingSpecification().queue(queue).exchange(EXCHANGE).routingKey(id.show()),
        ).awaitSingleOrNull() ?: error("Failed to initialize the binding between `$queue` and `$EXCHANGE`")
    }

    override suspend fun sendToComponent(payload: Send) {
        val message = OutboundMessage(
            EXCHANGE,
            id.show(), // TODO(find a better way to manage the routing key)
            Json.encodeToString(serializer, payload).toByteArray(),
        )
        sender.send(Mono.just(message)).awaitSingleOrNull()
    }
}

/**
 * Simple implementation for communicate (receive only) with another component using RabbitMQ.
 */
actual class SimpleRabbitmqReceiverCommunicator<Receive : Any, I : DeviceID>(
    private val type: KClass<Receive>,
    override val id: I,
    override val queue: String,
) : RabbitmqReceiverCommunicator<Receive, I>, KoinComponent {
    private val connection: Connection by inject(Connection::class.java)
    private var receiver: Receiver
    private val serializer = Json.serializersModule.serializer(type.createType())

    companion object {
        /**
         * Creates the communication without the need of passing the KClass.
         */
        inline operator fun <reified R : Any> invoke(id: DeviceID, queue: String) =
            SimpleRabbitmqReceiverCommunicator(R::class, id, queue)
    }

    init {
        val options = ReceiverOptions().connectionSupplier { connection }
        receiver = RabbitFlux.createReceiver(options)
    }

    @Suppress("UNCHECKED_CAST")
    override fun receiveFromComponent(): Flow<Receive> {
        return receiver.consumeAutoAck(queue).asFlow()
            .map { Json.decodeFromString(serializer, it.body.decodeToString()) as Receive }
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

    override suspend fun initialize() {
        sender.bindQueue(
            BindingSpecification().queue(queue).exchange(EXCHANGE).routingKey(id.toString()),
        ).awaitSingleOrNull() ?: error("Failed to bind `$queue` with `$EXCHANGE`")
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
