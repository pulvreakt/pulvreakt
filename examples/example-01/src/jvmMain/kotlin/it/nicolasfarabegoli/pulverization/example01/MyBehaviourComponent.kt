package it.nicolasfarabegoli.pulverization.example01

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.uchuhimo.konf.Config
import it.nicolasfarabegoli.pulverization.component.SendReceiveDeviceComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.nio.charset.StandardCharsets

actual class MyBehaviourComponent(override val deviceID: String) : SendReceiveDeviceComponent<OutgoingMessages, Unit, String>, KoinComponent {
    private val state: MyState by inject()
    private val behaviour: MyBehaviour by inject()
    private val config: Config by inject()
    private val channel: Channel
    private val connection: Connection
    private val deliverCallback = DeliverCallback { consumerTag, deliver ->
        val message = String(deliver.body, StandardCharsets.UTF_8)
        println("[$consumerTag] Received message: $message")
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
                channel.queueDeclare("sensors/$deviceID", false, false, false, null)

                channel.basicConsume("communication/$deviceID/outbox", true, "BehaviourConsumer", deliverCallback, cancelCallback)
                channel.basicConsume("sensors/$deviceID", true, "BehaviourConsumer", deliverCallback, cancelCallback)
                this.channel = channel
            }
        }
    }

    fun finalize() {
        channel.close()
        connection.close()
    }

    override fun sendToComponent(payload: OutgoingMessages, to: String) {
        when (payload) {
            is OutgoingMessages.CommunicationMessage ->
                channel.basicPublish("", "communication/$deviceID/inbox", null, payload.state.toByteArray())
        }
    }

    override fun receiveFromComponent(from: String) {}

    override suspend fun cycle() {
        val (newState, _, _, _) = behaviour(state.get(), "", emptySet())
        sendToComponent(OutgoingMessages.CommunicationMessage(newState), "")
    }
}
