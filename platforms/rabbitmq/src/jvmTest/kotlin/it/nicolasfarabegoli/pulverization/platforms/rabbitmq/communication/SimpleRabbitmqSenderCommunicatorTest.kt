package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.FunSpec
import io.kotest.koin.KoinExtension
import io.kotest.matchers.shouldBe
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.DeviceIDOps.toID
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import reactor.rabbitmq.RabbitFlux
import reactor.rabbitmq.ReceiverOptions
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class SimpleRabbitmqSenderCommunicatorTest : KoinTest, FunSpec() {
    private val koinModule = module {
        single<Connection> {
            val cf = ConnectionFactory()
            cf.useNio()
            cf.newConnection()
        }
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
                val options = ReceiverOptions().connectionSupplier { get() }
                val receiver = RabbitFlux.createReceiver(options)

                val sender = SimpleRabbitmqSenderCommunicator<String>(SensorsComponent to BehaviourComponent)
                sender.initialize()

                val result = async {
                    withTimeout(2.toDuration(DurationUnit.SECONDS)) {
                        receiver.consumeAutoAck("sensors/behaviour1").asFlow().take(1).collect { payload ->
                            Json.decodeFromString<String>(payload.body.decodeToString()) shouldBe "hello"
                        }
                    }
                }
                sender.sendToComponent("hello")
                result.await()

                // Release resources
                receiver.close()
                sender.finalize()
            }
        }
    }
}
