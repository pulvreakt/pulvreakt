package it.unibo.pulvreakt.mqtt

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import arrow.core.right
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.api.communication.protocol.Entity
import it.unibo.pulvreakt.api.communication.protocol.Protocol
import it.unibo.pulvreakt.errors.protocol.ProtocolError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.kodein.di.DI
import kotlin.random.Random

/**
 * MQTT Protocol implementation on JS side.
 */
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
    private val mainTopic = "PulvReAKt"

    override lateinit var di: DI
    private val logger = KotlinLogging.logger("MqttProtocol")

    private val registeredTopics = mutableMapOf<Pair<Entity, Entity>, String>()
    private val topicChannels = mutableMapOf<String, MutableSharedFlow<ByteArray>>()
    private lateinit var client: MqttClient

    override suspend fun setupChannel(
        source: Entity,
        destination: Entity
    ) {
        logger.debug { "Setting up channel for entity $source to $destination" }
        registeredTopics += (source to destination) to toTopics(source, destination)
        registeredTopics += (destination to source) to toTopics(destination, source)
        topicChannels += toTopics(source, destination) to MutableSharedFlow(1)
        topicChannels += toTopics(destination, source) to MutableSharedFlow(1)
    }

    override suspend fun writeToChannel(
        from: Entity,
        to: Entity,
        message: ByteArray
    ): Either<ProtocolError, Unit> = either {
        val topic = registeredTopics[Pair(from, to)]
        logger.debug { "Writing message $message to topic $topic" }

        ensureNotNull(topic) { ProtocolError.EntityNotRegistered(to) }

        val writeOptions = js("{}")
        writeOptions.qos = qos
        writeOptions.retain = retain
        writeOptions.keepalive = serverKeepAlive

        Either.catch {
            client.publish(
                topic,
                message.decodeToString(),
                options = writeOptions
            )
        }.mapLeft { ProtocolError.ProtocolException(it) }
    }

    override fun readFromChannel(
        from: Entity,
        to: Entity
    ): Either<ProtocolError, Flow<ByteArray>> = either {
        val candidateTopic = ensureNotNull(registeredTopics[Pair(from, to)]) {
            ProtocolError.EntityNotRegistered(from)
        }
        val channel = ensureNotNull(topicChannels[candidateTopic]) {
            ProtocolError.EntityNotRegistered(from)
        }
        logger.debug { "Reading from topic $candidateTopic" }
        channel.asSharedFlow()
    }

    override suspend fun initialize(): Either<ProtocolError, Unit> = either {
        Either.catch {
            logger.debug { "entering init" }
            val connectOptions = js(
                """
                    {
                        protocolId: 'MQTT',
                        protocolVersion: 5,
                        clean: false,"
                    }
                """.trimIndent()
            )
            connectOptions.username = this@MqttProtocol.username
            connectOptions.password = this@MqttProtocol.password
            connectOptions.clientId = Random.nextInt()
            logger.debug { "attempting to connect" }
            client = connect("ws://$host:$port/mqtt", options = connectOptions)

            logger.debug { "waiting to connect" }

            val subOptions = js("{}")
            subOptions.qos = qos

            client.on("connect") { _, _, _ ->
                logger.debug { "client initialized" }
                client.subscribe(
                    "$mainTopic/#",
                    options = subOptions
                ) { err: dynamic, granted: dynamic ->
                    if (!err as Boolean) {
                        logger.debug { "Subscribed to topic: ${granted.topic}" }
                    } else {
                        logger.debug { "Subscribe did not work: ${err.message}" }
                    }
                }
            }

            client.on("error") { error: dynamic ->
                logger.debug { "Error connecting to MQTT broker: ${error.message}" }
            }
            client.on("message") { topic: String, payload: dynamic, _ ->
                // payload is a js buffer, so it needs to be converted
                val msg = ByteArray(payload.length as Int)
                for (i in 0 until payload.length as Int) {
                    msg[i] = payload[i] as Byte
                }

                logger.debug { "Received message on topic $topic: ${msg.decodeToString()}" }
                topicChannels[topic]?.tryEmit(msg)
            }
        }
    }

    override fun setupInjector(kodein: DI) {
        di = kodein
    }

    override suspend fun finalize(): Either<ProtocolError, Unit> {
        client.end(force = true, options = js("{reasonCode : 0x00}"))
        logger.debug { "client finalized" }
        return Unit.right()
    }

    private fun toTopics(source: Entity, destination: Entity): String {
        return if (source.id != null && destination.id != null) {
            "$mainTopic/${source.entityName}/${destination.entityName}/${destination.id}"
        } else {
            "$mainTopic/${source.entityName}/${destination.entityName}"
        }
    }
}
