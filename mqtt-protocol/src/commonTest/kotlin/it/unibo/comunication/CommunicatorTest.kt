package it.unibo.comunication

import arrow.core.Either
import arrow.core.raise.either
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.api.communication.protocol.Entity
import it.unibo.pulvreakt.errors.protocol.ProtocolError
import it.unibo.pulvreakt.mqtt.MqttProtocol
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.time.Duration.Companion.seconds

object ProjectConfig : AbstractProjectConfig() {
    override val timeout = 50.seconds
}

@OptIn(ExperimentalCoroutinesApi::class)
class CommunicatorTest : StringSpec({

    val host = "localhost"
    val port = 1884
//    val password = "password"

    val sourceEntity = Entity("source")
    val destinationEntity = Entity("destination")

    "should initialize and finalize correctly" {
        val mqttProtocol = MqttProtocol(
            host = host,
            port = port,
//            username = "init",
//            password = password,
        )
        val initResult = mqttProtocol.initialize()
        initResult shouldBe Either.Right(Unit)

        mqttProtocol.setupChannel(sourceEntity, destinationEntity)

        val finalizeResult = mqttProtocol.finalize()
        finalizeResult shouldBe Either.Right(Unit)
    }

    "should fail to write to channel when entities are not registered" {
        val mqttProtocol = MqttProtocol(
            host = host,
            port = port,
//            username = "fail1",
//            password = password,
        )
        val initResult = mqttProtocol.initialize()
        initResult shouldBe Either.Right(Unit)
        val invalidSourceEntity = Entity("invalidSource")
        val invalidDestinationEntity = Entity("invalidDestination")
        val result = mqttProtocol.writeToChannel(
            invalidSourceEntity, invalidDestinationEntity, "error Test".encodeToByteArray())

        result shouldBe Either.Left(ProtocolError.EntityNotRegistered(invalidDestinationEntity))
        val finalizeResult = mqttProtocol.finalize()
        finalizeResult shouldBe Either.Right(Unit)
    }

    "should fail to read from channel when entities are not registered" {
        val mqttProtocol = MqttProtocol(
            host = host,
            port = port,
//            username = "fail2",
//            password = password,
        )
        val initResult = mqttProtocol.initialize()
        initResult shouldBe Either.Right(Unit)
        val invalidSourceEntity = Entity("invalidSource")
        val invalidDestinationEntity = Entity("invalidDestination")
        val flowResult = mqttProtocol.readFromChannel(
            invalidSourceEntity, invalidDestinationEntity)

        flowResult shouldBe Either.Left(ProtocolError.EntityNotRegistered(invalidSourceEntity))
        val finalizeResult = mqttProtocol.finalize()
        finalizeResult shouldBe Either.Right(Unit)
    }

    "should work correctly" {
        val mqttProtocol = MqttProtocol(
            host = host,
            port = port,
//            username = "user",
//            password = password,
        )
        val initResult = mqttProtocol.initialize()
        initResult shouldBe Either.Right(Unit)

        val message = "protocol Test"
        var read = "initialized"

        mqttProtocol.setupChannel(sourceEntity, destinationEntity)

        val receiveJob = launch(UnconfinedTestDispatcher()) {
            val resultCollect = either {
                val flowResult = mqttProtocol.readFromChannel(sourceEntity, destinationEntity).bind()
                flowResult.take(1).collect {
                    read = it.decodeToString()
                }
            }
            resultCollect shouldBe Either.Right(Unit)
        }

        val result = mqttProtocol.writeToChannel(
            sourceEntity, destinationEntity, message.encodeToByteArray())

        result shouldBe Either.Right(Unit)
        receiveJob.join()
        read shouldBe message
        val finalizeResult = mqttProtocol.finalize()
        finalizeResult shouldBe Either.Right(Unit)
    }
})
