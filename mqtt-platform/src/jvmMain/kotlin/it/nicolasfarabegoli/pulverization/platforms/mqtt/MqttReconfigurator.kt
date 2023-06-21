package it.nicolasfarabegoli.pulverization.platforms.mqtt

import co.touchlab.kermit.Logger
import it.nicolasfarabegoli.pulverization.runtime.context.ExecutionContext
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.NewConfiguration
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.Reconfigurator
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Implement the [Reconfigurator] interface relying on Mqtt as a platform for communication.
 */
actual class MqttReconfigurator actual constructor(
    private val hostname: String,
    private val port: Int,
    private val username: String,
    private val password: String,
    private val dispatcher: CoroutineDispatcher,
) : Reconfigurator, KoinComponent {
    private val executionContext: ExecutionContext by inject()
    private val logger = Logger.withTag("MqttCommunicator")

    private val mqttClient = MqttClient("tcp://$hostname:$port", MqttClient.generateClientId(), MemoryPersistence())
    private lateinit var reconfigurationTopic: String
    private val defaultQos = 2

    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("Koin module not initialized")

    override suspend fun reconfigure(newConfiguration: NewConfiguration) {
        val payload = Json.encodeToString(newConfiguration).encodeToByteArray()
        mqttClient.publish(reconfigurationTopic, payload, defaultQos, false)
    }

    override fun receiveReconfiguration(): Flow<NewConfiguration> = callbackFlow {
        val callback = object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                logger.e { "Connection Lost" }
                cancel(CancellationException("Connection lost", cause))
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                requireNotNull(message) { "Mqtt Message cannot be null" }
                trySend(Json.decodeFromString(message.payload.decodeToString()))
                    .onFailure { err -> logger.e { "Fail to emit message on the flow: ${err?.message}" } }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) = Unit
        }
        mqttClient.setCallback(callback)
        awaitClose { mqttClient.unsubscribe(reconfigurationTopic) }
    }

    override suspend fun initialize() {
        reconfigurationTopic = "reconfiguration/${executionContext.deviceID}"
        logger.d {
            "Connection parameters: [hostname=$hostname, port=$port, username=$username]"
        }
        val mqttConnectionOption = MqttConnectOptions().apply {
            userName = username
            password = this@MqttReconfigurator.password.toCharArray()
        }
        withContext(dispatcher) {
            mqttClient.connect(mqttConnectionOption)
            mqttClient.subscribe(reconfigurationTopic)
        }
    }

    override suspend fun finalize() = withContext(dispatcher) {
        mqttClient.disconnect()
    }
}
