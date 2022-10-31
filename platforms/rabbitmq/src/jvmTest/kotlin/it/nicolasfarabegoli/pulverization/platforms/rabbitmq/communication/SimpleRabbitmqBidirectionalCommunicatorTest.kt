package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.FunSpec
import io.kotest.koin.KoinExtension
import io.kotest.matchers.shouldBe
import it.nicolasfarabegoli.pulverization.core.DeviceIDOps.toID
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import reactor.rabbitmq.RabbitFlux
import reactor.rabbitmq.ReceiverOptions
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Serializable
data class Send(val value: String)

@Serializable
data class Receive(val value: String)

class SimpleRabbitmqBidirectionalCommunicatorTest : KoinTest, FunSpec() {
    private val koinModule = module {
        single<Connection> { MockConnectionFactory().newConnection() }
        single { RabbitmqContext("1".toID()) }
    }

    override fun extensions(): List<Extension> = listOf(KoinExtension(koinModule))

    private fun initQueue(conn: Connection, queue: String, exchange: String, routingKey: String): Channel {
        return conn.createChannel().use { channel ->
            channel.queueDeclare(queue, false, false, true, emptyMap())
            channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT)
            channel.queueBind(queue, exchange, routingKey)
            return@use channel
        }
    }

    init {
        context("Sender Receiver component that use RabbitMQ") {
            test("The component should send messages") {
                val queue = "queue"
                val exchange = "pulverization.exchange"
                val routingKey = "1"
                initQueue(get(), queue, exchange, routingKey)
                val receiverOptions = ReceiverOptions().connectionSupplier { get() }
                val receiver = RabbitFlux.createReceiver(receiverOptions)
                val result = async {
                    withTimeout(3.toDuration(DurationUnit.SECONDS)) {
                        receiver.consumeAutoAck(queue).asFlow().take(1).collect {
                            val decodedPayload = Json.decodeFromString<Send>(it.body.decodeToString())
                            decodedPayload.value shouldBe "hello, world"
                        }
                    }
                }
                val component = SimpleRabbitmqBidirectionalCommunication<Send, Receive>(exchange, queue)
                component.sendToComponent(Send("hello, world"))

                result.await()
            }
            test("The component should receive message") {
                val queue = "queue"
                val exchange = "pulverization.exchange"
                val routingKey = "1"
                initQueue(get(), queue, exchange, routingKey).use {
                    it.basicPublish(exchange, routingKey, null, Json.encodeToString(Receive("hello")).toByteArray())
                }
                val component = SimpleRabbitmqBidirectionalCommunication<Send, Receive>(exchange, queue)
                withContext(Dispatchers.Default) {
                    withTimeout(2.toDuration(DurationUnit.SECONDS)) {
                        component.receiveFromComponent().take(1).collect {
                            it.value shouldBe "hello"
                        }
                    }
                }
            }
        }
    }
}
