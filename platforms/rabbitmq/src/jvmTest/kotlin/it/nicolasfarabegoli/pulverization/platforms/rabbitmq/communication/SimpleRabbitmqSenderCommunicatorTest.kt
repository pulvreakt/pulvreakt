package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.FunSpec
import io.kotest.koin.KoinExtension
import io.kotest.matchers.shouldNotBe
import it.nicolasfarabegoli.pulverization.core.DeviceIDOps.toID
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.withTimeout
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import reactor.rabbitmq.RabbitFlux
import reactor.rabbitmq.ReceiverOptions
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class SimpleRabbitmqSenderCommunicatorTest : KoinTest, FunSpec() {
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
        context("Sender only component that use RabbitMQ") {
            test("The sender only component should create the queue correctly") {
                val queue = "test_queue"
                val exchange = "pulverization.exchange"
                val routingKey = "1"
                initQueue(get(), queue, exchange, routingKey)
                val options = ReceiverOptions().connectionSupplier { get() }
                val receiver = RabbitFlux.createReceiver(options)
                val result = async {
                    withTimeout(2.toDuration(DurationUnit.SECONDS)) {
                        receiver.consumeAutoAck(queue).asFlow().take(1).collect { payload ->
                            payload.body shouldNotBe null
                        }
                    }
                }
                SimpleRabbitmqSenderCommunicator<String>(exchange, queue).sendToComponent("")
                result.await()
            }
        }
    }
}
