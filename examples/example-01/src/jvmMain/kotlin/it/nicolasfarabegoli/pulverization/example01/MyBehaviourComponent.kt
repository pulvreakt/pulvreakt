package it.nicolasfarabegoli.pulverization.example01

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.uchuhimo.konf.Config
import it.nicolasfarabegoli.pulverization.component.SendReceiveDeviceComponent
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.nio.charset.StandardCharsets

actual class MyBehaviourComponent(override val id: String) :
    SendReceiveDeviceComponent<BehaviourOutgoingMessages, BehaviourIncomingMessages, String>,
    KoinComponent {
    private val state: MyState by inject()
    private val behaviour: MyBehaviour by inject()
    private val config: Config by inject()
    private val channel: Channel
    private val connection: Connection
    private var lastSensorRead: Map<String, Double> = emptyMap()
    private var incomingExport: List<Export> = emptyList()
    private val sensorsDeliverCallback = DeliverCallback { _, deliver ->
        val messages = Json.decodeFromString<SensorPayload.SensorResult>(String(deliver.body, StandardCharsets.UTF_8))
        lastSensorRead = lastSensorRead + (messages.sensorId to messages.value)
    }
    private val deliverCallback = DeliverCallback { consumerTag, deliver ->
        val message = Json.decodeFromString<List<Export>>(String(deliver.body, StandardCharsets.UTF_8))
        incomingExport = message
        println("[$consumerTag] Received export: $message")
    }
    private val cancelCallback = CancelCallback { consumerTag -> println("[$consumerTag] was canceled") }

    init {
        val connection = ConnectionFactory().apply { host = config[PulverizationConfig.hostname]; port = config[PulverizationConfig.port] }
        connection.newConnection().let { conn ->
            this.connection = conn
            conn.createChannel().let { channel ->
                channel.queueDeclare("communication/$id/inbox", false, false, false, null)
                channel.queueDeclare("communication/$id/outbox", false, false, false, null)
                channel.queueDeclare("sensors/$id", false, false, false, null)

                channel.basicConsume("communication/$id/outbox", true, "BehaviourCommunicationConsumer", deliverCallback, cancelCallback)
                channel.basicConsume("sensors/$id", true, "BehaviourSensorsConsumer", sensorsDeliverCallback, cancelCallback)
                this.channel = channel
            }
        }
    }

    override fun finalize() {
        channel.close()
        connection.close()
    }

    override fun sendToComponent(payload: BehaviourOutgoingMessages, to: String?) {
        when (payload) {
            is BehaviourOutgoingMessages.CommunicationMessage ->
                channel.basicPublish("", "communication/$id/inbox", null, Json.encodeToString(payload).toByteArray())

            is BehaviourOutgoingMessages.UpdateState -> state.update(payload.newState)
        }
    }

    override fun receiveFromComponent(from: String?): BehaviourIncomingMessages = TODO()

    override suspend fun cycle() {
        val (newState, export, _, _) = behaviour(state.get(), incomingExport, lastSensorRead)
        sendToComponent(BehaviourOutgoingMessages.UpdateState(newState))
        sendToComponent(BehaviourOutgoingMessages.CommunicationMessage(export))
    }
}
