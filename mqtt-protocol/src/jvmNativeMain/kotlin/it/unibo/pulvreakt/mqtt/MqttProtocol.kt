package it.unibo.pulvreakt.mqtt

import MQTTClient
import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import arrow.core.right
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.api.communication.protocol.Entity
import it.unibo.pulvreakt.api.communication.protocol.Protocol
import it.unibo.pulvreakt.errors.protocol.ProtocolError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import mqtt.MQTTVersion
import mqtt.Subscription
import mqtt.packets.Qos
import mqtt.packets.mqttv5.MQTT5Properties
import mqtt.packets.mqttv5.ReasonCode
import mqtt.packets.mqttv5.SubscriptionOptions
import org.kodein.di.DI
import socket.IOException

/**
 * MQTT Protocol implementation on Native and JVM side.
 */
@OptIn(ExperimentalUnsignedTypes::class)
@Suppress("ExperimentalUnsignedTypes")
actual class MqttProtocol actual constructor(
    private val host: String,
    private val port: Int,
    private val username: String?,
    private val password: String?,
    coroutineDispatcher: CoroutineDispatcher,
    private val serverKeepAlive: Int,
    private val retain: Boolean,
    private val qos: Int,
) : Protocol {
    override lateinit var di: DI

    override fun setupInjector(kodein: DI) {
        di = kodein
    }

    private val logger = KotlinLogging.logger("MqttProtocol")
    private val scope = CoroutineScope(coroutineDispatcher + Job())

    private val registeredTopics = mutableMapOf<Pair<Entity, Entity>, String>()
    private val topicChannels = mutableMapOf<String, MutableSharedFlow<ByteArray>>()
    private lateinit var listenerJob: Job
    private lateinit var client: MQTTClient
    private val mainTopic = "PulvReAKt"

    private val qosValue = when {
        qos <= 0 -> Qos.AT_MOST_ONCE
        qos == 1 -> Qos.AT_LEAST_ONCE
        else -> Qos.EXACTLY_ONCE
    }

    override suspend fun setupChannel(source: Entity, destination: Entity) {
        logger.debug { "Setting up channel for entity $source" }
        registeredTopics += (source to destination) to toTopics(source, destination)
        registeredTopics += (destination to source) to toTopics(destination, source)
        topicChannels += toTopics(source, destination) to MutableSharedFlow(1)
        topicChannels += toTopics(destination, source) to MutableSharedFlow(1)
    }

    override suspend fun writeToChannel(from: Entity, to: Entity, message: ByteArray): Either<ProtocolError, Unit> = coroutineScope {
        either {
            val topic = registeredTopics[Pair(from, to)]
            logger.debug { "Writing message $message to topic $topic" }

            ensureNotNull(topic) { ProtocolError.EntityNotRegistered(to) }

            Either.catch {
                client.publish(
                    retain = true,
                    qos = qosValue,
                    topic,
                    message.toUByteArray(),
                    MQTT5Properties(
                        serverKeepAlive = serverKeepAlive.toUInt(),
                        retainAvailable = if (retain) 1U else 0U,
                    )
                )
            }.mapLeft { ProtocolError.ProtocolException(it) }.bind()
        }
    }

    override fun readFromChannel(from: Entity, to: Entity): Either<ProtocolError, Flow<ByteArray>> = either {
        val candidateTopic = ensureNotNull(registeredTopics[Pair(from, to)]) { ProtocolError.EntityNotRegistered(from) }
        val channel = ensureNotNull(topicChannels[candidateTopic]) { ProtocolError.EntityNotRegistered(from) }
        logger.debug { "Reading from topic $candidateTopic" }
        channel.asSharedFlow()
    }

    override suspend fun initialize(): Either<ProtocolError, Unit> = coroutineScope {
        either {
            Either.catch {
                client = MQTTClient(
                    MQTTVersion.MQTT5,
                    host,
                    port,
                    tls = null,
                    userName = username,
                    password = password?.encodeToByteArray()?.toUByteArray(),
                    cleanStart = false,
                    webSocket = "/mqtt",
                ) {
                    logger.debug { "New message arrived on topic $it.topicName" }
                    try {
                        requireNotNull(it.payload) { "Message cannot be null" }
                        topicChannels[it.topicName]?.tryEmit(it.payload!!.toByteArray())
                    } catch (e: IllegalArgumentException) {
                        logger.debug(e) { "Payload was null for message on topic ${it.topicName}" }
                    }
                }

                client.subscribe(
                    listOf(
                        Subscription(
                            "$mainTopic/#",
                            SubscriptionOptions(qos = qosValue)
                        )
                    )
                )
            }.mapLeft {
                logger.debug(it) { "MQTT Client initialization failed" }
                ProtocolError.ProtocolException(it)
            }.bind()

            listenerJob = scope.launch {
                try {
                    logger.debug { "Client setup started" }
                    client.run()
                } catch (e: IOException) {
                    logger.debug(e) { "IO error running MQTT client, Caused by: '${e.message}'" }
                }
            }
        }
    }

    override suspend fun finalize(): Either<ProtocolError, Unit> {
        client.disconnect(ReasonCode.SUCCESS)
        scope.coroutineContext.cancelChildren()
        logger.debug { "client finalized" }
        return Unit.right()
    }

    /**
     * Creates the MQTT topic for the given entity.
     * @return the topic where the entity will receive messages.
     */
    private fun toTopics(source: Entity, destination: Entity): String {
        return if (source.id != null && destination.id != null) {
            "$mainTopic/${source.entityName}/${destination.entityName}/${destination.id}"
        } else {
            "$mainTopic/${source.entityName}/${destination.entityName}"
        }
    }
}
