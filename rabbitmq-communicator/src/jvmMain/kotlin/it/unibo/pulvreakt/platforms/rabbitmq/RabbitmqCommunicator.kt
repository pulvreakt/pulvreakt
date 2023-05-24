package it.unibo.pulvreakt.platforms.rabbitmq

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import it.unibo.pulvreakt.dsl.model.show
import it.unibo.pulvreakt.runtime.communication.Binding
import it.unibo.pulvreakt.runtime.communication.Communicator
import it.unibo.pulvreakt.runtime.communication.RemotePlace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import mu.KotlinLogging
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
 * Implement the [Communicator] interface relying on RabbitMQ as a platform for communications.
 */
actual class RabbitmqCommunicator actual constructor(
    private val hostname: String,
    private val port: Int,
    private val username: String,
    private val password: String,
    private val virtualHost: String,
) : Communicator {
    private lateinit var sender: Sender
    private lateinit var receiver: Receiver
    private lateinit var sendQueue: String
    private lateinit var receiveQueue: String
    private lateinit var sendRoutingKey: String
    private lateinit var receiveRoutingKey: String
    private val logger = KotlinLogging.logger("RabbitmqCommunicator")

    companion object {
        private const val EXCHANGE = "pulverization.exchange"
    }

    private fun initConnection(): Connection {
        logger.debug { "Setup RabbitMQ connection" }
        logger.debug {
            "Connection parameters: [hostname=$hostname, port=$port, username=$username, virtualhost=$virtualHost]"
        }
        val connectionFactory = ConnectionFactory()
        connectionFactory.useNio()
        connectionFactory.apply {
            host = hostname
            port = this@RabbitmqCommunicator.port
            username = this@RabbitmqCommunicator.username
            password = this@RabbitmqCommunicator.password
            virtualHost = this@RabbitmqCommunicator.virtualHost
        }
        return connectionFactory.newConnection()
    }

    override suspend fun setup(binding: Binding, remotePlace: RemotePlace?) {
        logger.info { "Setup RabbitMQ communicator from ${binding.first} and ${binding.second}" }
        requireNotNull(remotePlace) { "To initialize Rabbitmq the RemotePlace should not be null" }
        initConnection().apply {
            logger.debug { "Setup RabbitMQ sender and receiver" }
            val senderOptions = SenderOptions().connectionSupplier { this }
            val receiverOptions = ReceiverOptions().connectionSupplier { this }
            sender = RabbitFlux.createSender(senderOptions)
            receiver = RabbitFlux.createReceiver(receiverOptions)
        }
        sender.apply {
            logger.debug { "Declare exchange `$EXCHANGE`" }
            declareExchange(ExchangeSpecification.exchange(EXCHANGE).type("topic").durable(false))
                .awaitSingleOrNull() ?: error("Unable to declare exchange")

            sendQueue = "${binding.first.show()}/${remotePlace.where}/${remotePlace.who}"
            receiveQueue = "${remotePlace.where}/${binding.first.show()}/${remotePlace.who}"
            sendRoutingKey = "${remotePlace.who}.${remotePlace.where}.${binding.first.show()}"
            receiveRoutingKey = "${remotePlace.who}.${binding.first.show()}.${remotePlace.where}"

            logger.debug { "Declare queue $sendQueue" }
            declareQueue(QueueSpecification.queue(sendQueue).durable(false))
                .awaitSingleOrNull() ?: error("Unable to create the queue $sendQueue")

            logger.debug { "Declare queue $receiveQueue" }
            declareQueue(QueueSpecification.queue(receiveQueue).durable(false))
                .awaitSingleOrNull() ?: error("Unable to create the queue $receiveQueue")

            logger.debug { "Bind queue $sendQueue with exchange $EXCHANGE with routing key $sendRoutingKey" }
            bindQueue(BindingSpecification().queue(sendQueue).exchange(EXCHANGE).routingKey(sendRoutingKey))
                .awaitSingleOrNull() ?: error("Unable to bind $EXCHANGE with $sendQueue")

            logger.debug { "Bind queue $receiveQueue with exchange $EXCHANGE with routing key $receiveRoutingKey" }
            bindQueue(BindingSpecification().queue(receiveQueue).exchange(EXCHANGE).routingKey(receiveRoutingKey))
                .awaitSingleOrNull() ?: error("Unable to bind $EXCHANGE with $receiveQueue")
        }
    }

    override suspend fun finalize() {
        sender.close()
        receiver.close()
    }

    override suspend fun fireMessage(message: ByteArray) {
        val payload = OutboundMessage(EXCHANGE, sendRoutingKey, message)
        sender.send(Mono.just(payload)).awaitSingleOrNull()
    }

    override fun receiveMessage(): Flow<ByteArray> =
        receiver.consumeAutoAck(receiveQueue).map { it.body }.asFlow()
}