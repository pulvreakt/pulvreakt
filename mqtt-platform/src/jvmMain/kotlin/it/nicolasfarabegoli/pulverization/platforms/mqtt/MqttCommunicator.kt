package it.nicolasfarabegoli.pulverization.platforms.mqtt

import co.touchlab.kermit.Logger
import it.nicolasfarabegoli.pulverization.dsl.model.show
import it.nicolasfarabegoli.pulverization.runtime.communication.Binding
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

/**
 * Implement the [Communicator] interface relying on MQTT as a platform for communications.
 */
actual class MqttCommunicator actual constructor(
    private val hostname: String,
    private val port: Int,
    private val username: String,
    private val password: String,
    private val dispatcher: CoroutineDispatcher,
) : Communicator {
    private val logger = Logger.withTag("MqttCommunicator")
    private val mqttClient = MqttClient("tcp://$hostname:$port", MqttClient.generateClientId(), MemoryPersistence())
    private lateinit var sendTopic: String
    private lateinit var receiveTopic: String
    private val defaultQoS = 2

    override suspend fun setup(binding: Binding, remotePlace: RemotePlace?) {
        logger.i { "Setup RabbitMQ communicator from ${binding.first} and ${binding.second}" }
        logger.d {
            "Connection parameters: [hostname=$hostname, port=$port, username=$username]"
        }
        requireNotNull(remotePlace) { "Remote place cannot be null" }

        sendTopic = "${binding.first.show()}/${remotePlace.where}/${remotePlace.who}"
        receiveTopic = "${remotePlace.where}/${binding.first.show()}/${remotePlace.who}"
//        sendRoutingKey = "${remotePlace.who}.${remotePlace.where}.${binding.first.show()}"
//        receiveRoutingKey = "${remotePlace.who}.${binding.first.show()}.${remotePlace.where}"
        logger.i { "SendTopic: $sendTopic" }
        logger.i { "ReceiveTopic: $receiveTopic" }

        val mqttConnectionOption = MqttConnectOptions().apply {
            userName = username
            password = this@MqttCommunicator.password.toCharArray()
        }

        withContext(dispatcher) {
            mqttClient.connect(mqttConnectionOption)
            logger.i { "MQTT client connected to the broker" }
            mqttClient.subscribe(receiveTopic, defaultQoS)
            logger.i { "MQTT client subscribed to the topic" }
        }
    }

    override suspend fun finalize() = withContext(dispatcher) {
        mqttClient.disconnect()
    }

    override suspend fun fireMessage(message: ByteArray) = withContext(dispatcher) {
        mqttClient.publish(sendTopic, message, defaultQoS, false)
    }

    override fun receiveMessage(): Flow<ByteArray> = callbackFlow {
        val callback = object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                logger.e { "Connection Lost" }
                cancel(CancellationException("Connection lost", cause))
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                requireNotNull(message) { "Mqtt Message cannot be null" }
                trySend(message.payload)
                    .onFailure { err -> logger.e { "Fail to emit message on the flow: ${err?.message}" } }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) = Unit
        }
        mqttClient.setCallback(callback)
        awaitClose { mqttClient.unsubscribe(receiveTopic) }
    }
}
