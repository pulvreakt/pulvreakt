package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import com.rabbitmq.client.Connection
import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
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
import reactor.rabbitmq.ExchangeSpecification
import reactor.rabbitmq.OutboundMessage
import reactor.rabbitmq.QueueSpecification
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
    override val communicationType: Pair<PulverizedComponentType, PulverizedComponentType>,
) : RabbitmqSenderCommunicator<Send>, KoinComponent {
    actual override val context: RabbitmqContext by inject()
    private val connection: Connection by inject(Connection::class.java)
    private val serializer = Json.serializersModule.serializer(type.createType())
    private val sender: Sender

    private lateinit var queue: String
    private lateinit var exchange: String

    actual companion object {
        /**
         * Creates the communication without the need of passing the KClass.
         */
        actual inline operator fun <reified S : Any> invoke(
            communicationType: Pair<PulverizedComponentType, PulverizedComponentType>,
        ) = SimpleRabbitmqSenderCommunicator(S::class, communicationType)
    }

    init {
        val options = SenderOptions().connectionSupplier { connection }
        sender = RabbitFlux.createSender(options)
    }

    override suspend fun initialize() {
        val (_queue, _exchange) = getQueueAndExchange(communicationType)
        queue = _queue + context.id.show()
        exchange = _exchange

        sender.declareExchange(ExchangeSpecification.exchange(exchange).type("topic").durable(false))
            .awaitSingleOrNull()
        sender.declareQueue(QueueSpecification.queue(queue)).awaitSingleOrNull()
        sender.bindQueue(
            BindingSpecification().queue(queue).exchange(exchange).routingKey(context.id.show()),
        ).awaitSingleOrNull() ?: error("Failed to initialize the binding between `$queue` and `$exchange`")
    }

    override suspend fun finalize() {
        sender.close()
    }

    override suspend fun sendToComponent(payload: Send) {
        val message = OutboundMessage(
            exchange,
            context.id.show(),
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
    override val communicationType: Pair<PulverizedComponentType, PulverizedComponentType>,
) : RabbitmqReceiverCommunicator<Receive>, KoinComponent {
    actual override val context: RabbitmqContext by inject()
    private val connection: Connection by inject(Connection::class.java)
    private val serializer = Json.serializersModule.serializer(type.createType())
    private val receiver: Receiver
    private val sender: Sender

    private lateinit var queue: String
    private lateinit var exchange: String

    actual companion object {
        /**
         * Creates the communication without the need of passing the KClass.
         */
        actual inline operator fun <reified R : Any> invoke(
            communicationType: Pair<PulverizedComponentType, PulverizedComponentType>,
        ) = SimpleRabbitmqReceiverCommunicator(R::class, communicationType)
    }

    init {
        val receiverOptions = ReceiverOptions().connectionSupplier { connection }
        val senderOptions = SenderOptions().connectionSupplier { connection }
        receiver = RabbitFlux.createReceiver(receiverOptions)
        sender = RabbitFlux.createSender(senderOptions)
    }

    override suspend fun initialize() {
        val (_queue, _exchange) = getQueueAndExchange(communicationType)
        queue = _queue + context.id.show()
        exchange = _exchange

        sender.declareQueue(QueueSpecification.queue(queue).durable(false)).awaitSingleOrNull()
            ?: error("Error creating the queue $queue")
        sender.declareExchange(ExchangeSpecification.exchange(exchange).type("topic").durable(false))
            .awaitSingleOrNull() ?: error("Error creating the exchange $exchange")
        sender.bindQueue(BindingSpecification.binding(exchange, context.id.show(), queue)).awaitSingleOrNull()
            ?: error("Error creating the binding between '$queue' and '$exchange'")
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
    override val communicationType: Pair<PulverizedComponentType, PulverizedComponentType>,
) : RabbitmqBidirectionalCommunicator<Send, Receive>, KoinComponent {
    actual override val context: RabbitmqContext by inject()
    private val connection: Connection by inject(Connection::class.java)
    private val sendSerializer = Json.serializersModule.serializer(kSend.createType())
    private val receiveDeserializer = Json.serializersModule.serializer(kReceive.createType())
    private val sender: Sender
    private val receiver: Receiver

    private lateinit var sendQueue: String
    private lateinit var receiveQueue: String
    private lateinit var exchange: String
    private lateinit var sendRoutingKey: String
    private lateinit var receiveRoutingKey: String

    actual companion object {
        /**
         * Creates the communication without the need of passing the KClass.
         */
        actual inline operator fun <reified S : Any, reified R : Any> invoke(
            communicationType: Pair<PulverizedComponentType, PulverizedComponentType>,
        ) = SimpleRabbitmqBidirectionalCommunication(S::class, R::class, communicationType)
    }

    init {
        val senderOptions = SenderOptions().connectionSupplier { connection }
        val receiverOptions = ReceiverOptions().connectionSupplier { connection }
        sender = RabbitFlux.createSender(senderOptions)
        receiver = RabbitFlux.createReceiver(receiverOptions)
    }

    override suspend fun initialize() {
        val (_sendQueue, _) = getQueueAndExchange(communicationType)
        val (_receiveQueue, _exchange) = getQueueAndExchange(communicationType.second to communicationType.first)

        sendQueue = _sendQueue + context.id.show()
        receiveQueue = _receiveQueue + context.id.show()
        exchange = _exchange
        sendRoutingKey = "${context.id.show()}.${getName(communicationType.second)}"
        receiveRoutingKey = "${context.id.show()}.${getName(communicationType.first)}"

        sender.declareExchange(ExchangeSpecification.exchange(exchange).type("topic").durable(false))
            .awaitSingleOrNull() ?: error("Failed to declare the exchange $exchange")
        sender.declareQueue(QueueSpecification.queue(sendQueue).durable(false)).awaitSingleOrNull()
            ?: error("Failed to declare queue $sendQueue")
        sender.declareQueue(QueueSpecification.queue(receiveQueue).durable(false)).awaitSingleOrNull()
            ?: error("Failed to declare queue $receiveQueue")
        sender.bindQueue(
            BindingSpecification().queue(sendQueue).exchange(exchange).routingKey(sendRoutingKey),
        ).awaitSingleOrNull() ?: error("Failed to bind `$sendQueue` with `$exchange`")
        sender.bindQueue(
            BindingSpecification().queue(receiveQueue).exchange(exchange).routingKey(receiveRoutingKey),
        ).awaitSingleOrNull() ?: error("Failed to bind `$receiveQueue` with `$exchange`")
    }

    override suspend fun sendToComponent(payload: Send) {
        val message = OutboundMessage(
            exchange,
            sendRoutingKey,
            Json.encodeToString(sendSerializer, payload).toByteArray(),
        )
        sender.send(Mono.just(message)).awaitSingleOrNull()
    }

    @Suppress("UNCHECKED_CAST")
    override fun receiveFromComponent(): Flow<Receive> {
        println("Setup!")
        return receiver.consumeAutoAck(receiveQueue).asFlow()
            .map { Json.decodeFromString(receiveDeserializer, it.body.decodeToString()) as Receive }
    }
}

internal fun getQueueAndExchange(
    communicationType: Pair<PulverizedComponentType, PulverizedComponentType>,
): Pair<String, String> {
    val (from, to) = communicationType
    val (fromName, toName) = getName(from) to getName(to)
    return ("$fromName/$toName") to sortAndCombine(fromName, toName) + ".xch"
}

internal fun sortAndCombine(s1: String, s2: String): String = if (s1 > s2) "$s1.$s2" else "$s2.$s1"

internal fun getName(component: PulverizedComponentType): String =
    when (component) {
        ActuatorsComponent -> "actuators"
        BehaviourComponent -> "behaviour"
        CommunicationComponent -> "communication"
        SensorsComponent -> "sensors"
        StateComponent -> "state"
    }
