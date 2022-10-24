package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.FunSpec
import io.kotest.koin.KoinExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import it.nicolasfarabegoli.pulverization.core.DeviceIDOps.toID
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Serializable
data class Foo(val i: Int)

class SimpleRabbitmqReceiverCommunicatorTest : KoinTest, FunSpec() {
    private val koinModule = module {
        single<Connection> { MockConnectionFactory().newConnection() }
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
        context("Receiver only component that use RabbitMQ") {
            test("The receiver should receive the message") {
                val queue = "queue"
                val exchange = "exc"
                val routingKey = "r.r"
                val payload = Foo(12)
                initQueue(get(), queue, exchange, routingKey).use { channel ->
                    channel.basicPublish(exchange, routingKey, null, Json.encodeToString(payload).toByteArray())
                }
                val receiver = SimpleRabbitmqReceiverCommunicator<Foo>("1".toID(), queue)
                withTimeout(3.toDuration(DurationUnit.SECONDS)) {
                    receiver.receiveFromComponent().take(1).collect {
                        it shouldNotBe null
                        it.i shouldBe 12
                    }
                }
            }
        }
    }
}
