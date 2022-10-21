package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Connection
import io.kotest.common.ExperimentalKotest
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.FunSpec
import io.kotest.koin.KoinExtension
import io.kotest.matchers.shouldNotBe
import it.nicolasfarabegoli.pulverization.core.DeviceIDOps.toID
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.builtins.serializer
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import reactor.rabbitmq.RabbitFlux
import reactor.rabbitmq.ReceiverOptions
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalKotest::class)
class SimpleRabbitmqCommunicatorTest : KoinTest, FunSpec() {
    private val koinModule = module {
        single<Connection> { MockConnectionFactory().newConnection() }
    }

    override fun extensions(): List<Extension> = listOf(KoinExtension(koinModule))

    init {
        context("Pulverized system with RabbitMQ as communicator").config(enabled = false) {
            test("The sender only component should create the queue correctly") {
                val connection: Connection = get()
                connection.createChannel().use {
                    it.exchangeDeclare("exc", BuiltinExchangeType.TOPIC)
                    it.queueDeclare("test_queue", false, false, true, emptyMap())
                }
                val options = ReceiverOptions().connectionSupplier { connection }
                val receiver = RabbitFlux.createReceiver(options)
                val result = async {
                    withTimeout(2.toDuration(DurationUnit.SECONDS)) {
                        receiver.consumeAutoAck("test_queue").asFlow().collectLatest { payload ->
                            payload shouldNotBe null
                        }
                    }
                }
                SimpleRabbitmqSenderCommunicator(
                    "1".toID(),
                    "test_queue",
                    String.serializer(),
                ).sendToComponent("")
                result.await()
            }
        }
    }
}
