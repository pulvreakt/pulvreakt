package it.nicolasfarabegoli.pulverization.example01

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.uchuhimo.konf.Config
import it.nicolasfarabegoli.pulverization.component.SendReceiveDeviceComponent
import it.nicolasfarabegoli.pulverization.core.Communication
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class MyCommunication(
    override val id: String,
    private val neighboursList: List<String> = emptyList(),
) : Communication<Export, List<Export>, String>, KoinComponent {
    private val config: Config by inject()
    private val channel: Channel
    private val connection: Connection
    private var neighboursMessages: Map<String, Export> = emptyMap()

    private val deliverCallback = DeliverCallback { _, deliver ->
        val message = Json.decodeFromString<Export>(deliver.body.decodeToString())
        neighboursMessages = neighboursMessages + (message.deviceId to message)
    }
    private val cancelCallback = CancelCallback { consumerTag ->
        println("[$consumerTag] was canceled")
    }

    init {
        println("My neighbours: $neighboursList")
        val connection = ConnectionFactory()
            .apply { host = config[PulverizationConfig.hostname]; port = config[PulverizationConfig.port] }
        connection.newConnection().let { conn ->
            this.connection = conn
            conn.createChannel().let { channel ->
                neighboursList.forEach {
                    channel.queueDeclare("neighbours/$it", false, false, false, null)
                }
                channel.queueDeclare("neighbours/$id", false, false, false, null)
                channel.basicConsume("neighbours/$id", true, "CommunicatorConsumer", deliverCallback, cancelCallback)
                this.channel = channel
            }
        }
    }

    fun finalize() {
        channel.close()
        connection.close()
    }

    override fun send(payload: Export) {
        neighboursList.forEach {
            channel.basicPublish("", "neighbours/$it", null, Json.encodeToString(payload).toByteArray())
        }
    }

    override fun receive(): List<Export> = neighboursMessages.values.toList()
}

actual class MyCommunicationComponent(override val id: String) :
    SendReceiveDeviceComponent<List<Export>, Export, String>, KoinComponent {
    private val myCommunication: MyCommunication by inject()
    private val config: Config by inject()
    private val channel: Channel
    private val connection: Connection
    private var lastMessage: Export? = null
    private val deliverCallback = DeliverCallback { _, deliver ->
        val msg = Json.decodeFromString<BehaviourOutgoingMessages.CommunicationMessage>(deliver.body.decodeToString())
        lastMessage = msg.export
    }
    private val cancelCallback = CancelCallback { consumerTag ->
        println("[$consumerTag] was canceled")
    }

    init {
        val connection = ConnectionFactory().apply {
            host = config[PulverizationConfig.hostname]; port = config[PulverizationConfig.port]
        }
        connection.newConnection().let { conn ->
            this.connection = conn
            conn.createChannel().let { channel ->
                channel.queueDeclare("communication/$id/inbox", false, false, false, null)
                channel.queueDeclare("communication/$id/outbox", false, false, false, null)
                channel.basicConsume(
                    "communication/$id/inbox",
                    true,
                    "CommComponentConsumer",
                    deliverCallback,
                    cancelCallback,
                )
                this.channel = channel
            }
        }
    }

    override fun finalize() {
        channel.close()
        connection.close()
        myCommunication.finalize()
    }

    override fun sendToComponent(payload: List<Export>, to: String?) {
        channel.basicPublish("", "communication/$id/outbox", null, Json.encodeToString(payload).toByteArray())
    }

    override fun receiveFromComponent(from: String?): Export? = lastMessage

    override suspend fun cycle() {
        TODO("FIX")
//        receiveFromComponent()?.let {
//            myCommunication.send(it)
//            lastMessage = null // Prevent sending the same data multiple times
//        }
//        val receivedExports = myCommunication.receive()
//        sendToComponent(receivedExports)
    }
}
