package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import com.rabbitmq.client.Connection
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
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
actual class SimpleRabbitmqSenderCommunicator<Send : Any> actual constructor(
    type: KClass<Send>,
    override val queue: String,
) : RabbitmqSenderCommunicator<Send>, KoinComponent {
    actual override val context: RabbitmqContext by inject()
    private val connection: Connection by inject(Connection::class.java)
    private val sender: Sender
    private val serializer = Json.serializersModule.serializer(type.createType())

    actual companion object {
        /**
         * Creates the communication without the need of passing the KClass.
         */
        actual inline operator fun <reified S : Any> invoke(queue: String) =
            SimpleRabbitmqSenderCommunicator(S::class, queue)

        private const val EXCHANGE = "pulverization.exchange"
    }

    init {
        val options = SenderOptions().connectionSupplier { connection }
        sender = RabbitFlux.createSender(options)
    }

    override suspend fun initialize() {
        sender.bindQueue(
            BindingSpecification().queue(queue).exchange(EXCHANGE).routingKey(context.id.show()),
        ).awaitSingleOrNull() ?: error("Failed to initialize the binding between `$queue` and `$EXCHANGE`")
    }

    override suspend fun sendToComponent(payload: Send) {
        val message = OutboundMessage(
            EXCHANGE,
            context.id.show(), // TODO(find a better way to manage the routing key)
            Json.encodeToString(serializer, payload).toByteArray(),
        )
        sender.send(Mono.just(message)).awaitSingleOrNull()
    }
}

/**
 * Simple implementation for communicate (receive only) with another component using RabbitMQ.
 */
actual class SimpleRabbitmqReceiverCommunicator<Receive : Any> actual constructor(
    type: KClass<Receive>,
    override val queue: String,
) : RabbitmqReceiverCommunicator<Receive>, KoinComponent {
    actual override val context: RabbitmqContext by inject()
    private val connection: Connection by inject(Connection::class.java)
    private var receiver: Receiver
    private val serializer = Json.serializersModule.serializer(type.createType())

    actual companion object {
        /**
         * Creates the communication without the need of passing the KClass.
         */
        actual inline operator fun <reified R : Any> invoke(queue: String) =
            SimpleRabbitmqReceiverCommunicator(R::class, queue)
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
actual class SimpleRabbitmqBidirectionalCommunication<Send : Any, Receive : Any> actual constructor(
    kSend: KClass<Send>,
    kReceive: KClass<Receive>,
    override val queue: String,
) : RabbitmqBidirectionalCommunicator<Send, Receive>, KoinComponent {
    actual override val context: RabbitmqContext by inject()
    private val connection: Connection by inject(Connection::class.java)
    private val sendSerializer = Json.serializersModule.serializer(kSend.createType())
    private val receiveDeserializer = Json.serializersModule.serializer(kReceive.createType())
    private val sender: Sender
    private val receiver: Receiver

    actual companion object {
        /**
         * Creates the communication without the need of passing the KClass.
         */
        actual inline operator fun <reified S : Any, reified R : Any> invoke(queue: String) =
            SimpleRabbitmqBidirectionalCommunication(S::class, R::class, queue)

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
            BindingSpecification().queue(queue).exchange(EXCHANGE).routingKey(context.id.toString()),
        ).awaitSingleOrNull() ?: error("Failed to bind `$queue` with `$EXCHANGE`")
    }

    override suspend fun sendToComponent(payload: Send) {
        val message = OutboundMessage(
            EXCHANGE,
            context.id.show(),
            Json.encodeToString(sendSerializer, payload).toByteArray(),
        )
        sender.send(Mono.just(message)).awaitSingleOrNull()
    }

    @Suppress("UNCHECKED_CAST")
    override fun receiveFromComponent(): Flow<Receive> {
        return receiver.consumeAutoAck(queue).asFlow()
            .map { Json.decodeFromString(receiveDeserializer, it.body.decodeToString()) as Receive }
    }
}
