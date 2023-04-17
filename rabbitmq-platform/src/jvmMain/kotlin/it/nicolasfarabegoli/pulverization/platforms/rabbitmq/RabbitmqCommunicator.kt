package it.nicolasfarabegoli.pulverization.platforms.rabbitmq

import co.touchlab.kermit.Logger
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import it.nicolasfarabegoli.pulverization.dsl.model.show
import it.nicolasfarabegoli.pulverization.runtime.communication.Binding
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
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
    private val logger = Logger.withTag("RabbitmqCommunicator")

    companion object {
        private const val EXCHANGE = "pulverization.exchange"
    }

    private fun initConnection(): Connection {
        logger.d { "Setup RabbitMQ connection" }
        logger.d {
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
        logger.i { "Setup RabbitMQ communicator from ${binding.first} and ${binding.second}" }
        requireNotNull(remotePlace) { "To initialize Rabbitmq the RemotePlace should not be null" }
        initConnection().apply {
            logger.d { "Setup RabbitMQ sender and receiver" }
            val senderOptions = SenderOptions().connectionSupplier { this }
            val receiverOptions = ReceiverOptions().connectionSupplier { this }
            sender = RabbitFlux.createSender(senderOptions)
            receiver = RabbitFlux.createReceiver(receiverOptions)
        }
        sender.apply {
            logger.d { "Declare exchange `$EXCHANGE`" }
            declareExchange(ExchangeSpecification.exchange(EXCHANGE).type("topic").durable(false))
                .awaitSingleOrNull() ?: error("Unable to declare exchange")

            sendQueue = "${binding.first.show()}/${remotePlace.where}/${remotePlace.who}"
            receiveQueue = "${remotePlace.where}/${binding.first.show()}/${remotePlace.who}"
            sendRoutingKey = "${remotePlace.who}.${remotePlace.where}.${binding.first.show()}"
            receiveRoutingKey = "${remotePlace.who}.${binding.first.show()}.${remotePlace.where}"

            logger.d { "Declare queue $sendQueue" }
            declareQueue(QueueSpecification.queue(sendQueue).durable(false))
                .awaitSingleOrNull() ?: error("Unable to create the queue $sendQueue")

            logger.d { "Declare queue $receiveQueue" }
            declareQueue(QueueSpecification.queue(receiveQueue).durable(false))
                .awaitSingleOrNull() ?: error("Unable to create the queue $receiveQueue")

            logger.d { "Bind queue $sendQueue with exchange $EXCHANGE with routing key $sendRoutingKey" }
            bindQueue(BindingSpecification().queue(sendQueue).exchange(EXCHANGE).routingKey(sendRoutingKey))
                .awaitSingleOrNull() ?: error("Unable to bind $EXCHANGE with $sendQueue")

            logger.d { "Bind queue $receiveQueue with exchange $EXCHANGE with routing key $receiveRoutingKey" }
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
