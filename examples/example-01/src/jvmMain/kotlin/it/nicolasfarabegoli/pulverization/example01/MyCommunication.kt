package it.nicolasfarabegoli.pulverization.example01

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.uchuhimo.konf.Config
import it.nicolasfarabegoli.pulverization.component.SendReceiveDeviceComponent
import it.nicolasfarabegoli.pulverization.core.Communication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.nio.charset.StandardCharsets

actual class MyCommunication(
    override val id: String,
    private val neighboursList: List<String> = emptyList(),
) : Communication<String, Map<String, String>, String>, KoinComponent {
    private val config: Config by inject()
    private val channel: Channel
    private val connection: Connection
    private var neighboursMessages: Map<String, String> = emptyMap()

    private val deliverCallback = DeliverCallback { consumerTag, deliver ->
        val message = String(deliver.body, StandardCharsets.UTF_8)
        "(.+) - .+".toRegex().find(message)?.destructured?.let { (neighbourId) ->
            println("[$consumerTag] Received message from $neighbourId: $message")
            neighboursMessages = neighboursMessages + (neighbourId to message)
        }
    }
    private val cancelCallback = CancelCallback { consumerTag ->
        println("[$consumerTag] was canceled")
    }

    init {
        println("My neighbours: $neighboursList")
        val connection = ConnectionFactory().apply { host = config[PulverizationConfig.hostname]; port = config[PulverizationConfig.port] }
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

    override fun send(payload: String) {
        neighboursList.forEach { channel.basicPublish("", "neighbours/$it", null, payload.toByteArray(StandardCharsets.UTF_8)) }
    }

    override fun receive(): Map<String, String> = neighboursMessages
}

actual class MyCommunicationComponent(override val deviceID: String) :
    SendReceiveDeviceComponent<Map<String, String>, String, String>, KoinComponent {
    private val myCommunication: MyCommunication by inject()
    private val config: Config by inject()
    private val channel: Channel
    private val connection: Connection
    private var lastMessage: String = ""
    private val deliverCallback = DeliverCallback { consumerTag, deliver ->
        lastMessage = String(deliver.body, StandardCharsets.UTF_8)
        println("[$consumerTag] Received message: $lastMessage")
    }
    private val cancelCallback = CancelCallback { consumerTag ->
        println("[$consumerTag] was canceled")
    }

    init {
        val connection = ConnectionFactory().apply { host = config[PulverizationConfig.hostname]; port = config[PulverizationConfig.port] }
        connection.newConnection().let { conn ->
            this.connection = conn
            conn.createChannel().let { channel ->
                channel.queueDeclare("communication/$deviceID/inbox", false, false, false, null)
                channel.queueDeclare("communication/$deviceID/outbox", false, false, false, null)
                channel.basicConsume("communication/$deviceID/inbox", true, "CommComponentConsumer", deliverCallback, cancelCallback)
                this.channel = channel
            }
        }
    }

    fun finalize() {
        channel.close()
        connection.close()
        myCommunication.finalize()
    }

    override fun sendToComponent(payload: Map<String, String>, to: String) {
        channel.basicPublish("", "communication/$deviceID/outbox", null, payload.toString().toByteArray())
    }

    override fun receiveFromComponent(from: String): String = lastMessage

    override suspend fun cycle() {
        val r = receiveFromComponent("")
        myCommunication.send(r)
        val rr = myCommunication.receive()
        sendToComponent(rr, "")
    }
}
