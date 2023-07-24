package it.unibo.pulvreakt.mqtt

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import arrow.core.right
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.core.context.Context
import it.unibo.pulvreakt.core.protocol.Entity
import it.unibo.pulvreakt.core.protocol.Protocol
import it.unibo.pulvreakt.core.protocol.errors.ProtocolError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.eclipse.paho.mqttv5.client.MqttAsyncClient
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence
import org.eclipse.paho.mqttv5.common.MqttMessage
import org.eclipse.paho.mqttv5.common.MqttSubscription
import org.kodein.di.DI
import org.kodein.di.instance

/**
 * MQTT Protocol implementation on JVM side.
 */
actual class MqttProtocol actual constructor(
    host: String,
    port: Int,
    private val username: String?,
    private val password: String?,
    private val coroutineDispatcher: CoroutineDispatcher,
) : Protocol {

    override lateinit var di: DI
    private val context by instance<Context>()
    private val deviceId by lazy { context.deviceId }
    private val logger = KotlinLogging.logger("MqttProtocol")

    private val registeredTopics = mutableMapOf<Entity, String>()
    private val mqttClient = MqttAsyncClient("tcp://$host:$port", "MqttProtocol[$deviceId]", MemoryPersistence())
    private val connectionOptions = MqttConnectionOptions().apply {
        isCleanStart = false
        userName = username
        password = this@MqttProtocol.password?.encodeToByteArray()
    }

    override suspend fun setupChannel(entity: Entity) {
        registeredTopics += entity to entity.toTopics()
    }

    override suspend fun writeToChannel(to: Entity, message: ByteArray): Either<ProtocolError, Unit> = coroutineScope {
        either {
            val topic = registeredTopics[to]
            ensureNotNull(topic) { ProtocolError.EntityNotRegistered(to) }
            val mqttMessage = MqttMessage(message).apply { qos = 2 }
            val catchResult = Either.catch {
                async(coroutineDispatcher) {
                    mqttClient.publish(topic, mqttMessage).waitForCompletion()
                }.await()
            }
            catchResult.mapLeft { ProtocolError.ProtocolException(it) }.bind()
        }
    }

    override fun readFromChannel(from: Entity): Either<ProtocolError, Flow<ByteArray>> = either {
        val candidateTopic = registeredTopics[from]
        ensureNotNull(candidateTopic) { ProtocolError.EntityNotRegistered(from) }
        val subscription = MqttSubscription(candidateTopic, 2)
        callbackFlow {
            mqttClient.subscribe(subscription) { _, message ->
                trySend(message.payload).onFailure { logger.error("Fail to emit message on the flow: ${it?.message}") }
            }
            awaitClose { mqttClient.unsubscribe(candidateTopic) }
        }
    }

    override suspend fun initialize(): Either<ProtocolError, Unit> = coroutineScope {
        Either.catch {
            async(coroutineDispatcher) {
                mqttClient.connect(connectionOptions).waitForCompletion()
            }.await()
        }.mapLeft { ProtocolError.ProtocolException(it) }
    }

    override suspend fun finalize(): Either<ProtocolError, Unit> {
        mqttClient.close()
        return Unit.right()
    }

    override fun setupInjector(kodein: DI) {
        di = kodein
    }

    /**
     * Creates the MQTT topic for the given entity.
     * @return the topic where the entity will receive messages.
     */
    private fun Entity.toTopics(): String {
        return if (this.id != null) {
            "inbox/${this.entityName}/${this.id}"
        } else {
            "inbox/${this.entityName}"
        }
    }
}
