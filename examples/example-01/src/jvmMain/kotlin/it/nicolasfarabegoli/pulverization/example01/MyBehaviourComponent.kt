package it.nicolasfarabegoli.pulverization.example01

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.nio.charset.StandardCharsets

actual class MyBehaviourComponent(override val deviceID: String) : DeviceComponent<OutgoingMessages, Unit, String>, KoinComponent {
    private val state: MyState by inject()
    private val behaviour: MyBehaviour by inject()
    private val channel: Channel
    private val deliverCallback = DeliverCallback { consumerTag, deliver ->
        val message = String(deliver.body, StandardCharsets.UTF_8)
        println("[$consumerTag] Received message: $message")
    }
    private val cancelCallback = CancelCallback { consumerTag ->
        println("[$consumerTag] was canceled")
    }

    init {
        val connection = ConnectionFactory()
        connection.newConnection("amqp://guest:guest@localhost:5672/").let { conn ->
            conn.createChannel().let { channel ->
                channel.queueDeclare("communication/$deviceID/inbox", false, false, false, null)
                channel.queueDeclare("communication/$deviceID/outbox", false, false, false, null)
                channel.basicConsume("communication/$deviceID/outbox", true, "SimpleConsumer", deliverCallback, cancelCallback)
                this.channel = channel
            }
        }
    }

    override fun sendToComponent(payload: OutgoingMessages, to: String) {
        when (payload) {
            is OutgoingMessages.SendMyState -> channel.basicPublish("", "communication/$deviceID/inbox", null, payload.state.toByteArray())
        }
    }

    override fun receiveFromComponent(from: String) { }

    override suspend fun cycle() {
        val result = behaviour(state.get(), "", emptySet()) // How destructure the result like Scala?
        sendToComponent(OutgoingMessages.SendMyState(result.newState), "")
    }
}
