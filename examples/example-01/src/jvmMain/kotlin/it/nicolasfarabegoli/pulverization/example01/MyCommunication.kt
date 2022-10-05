package it.nicolasfarabegoli.pulverization.example01

import com.rabbitmq.client.AMQP.Basic.Deliver
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.Communication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.nio.charset.StandardCharsets

actual class MyCommunication : Communication<String, String> {
    override fun send(payload: String) {
        println("Send to neighbours: $payload")
    }

    override fun receive(): String {
        return "FF"
    }
}

actual class MyCommunicationComponent(override val deviceID: String) : DeviceComponent<String, String, String>, KoinComponent {
    private val myCommunication: MyCommunication by inject()
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
                // TODO: declare neighbours queues
                channel.basicConsume("communication/$deviceID/inbox", true, "SimpleConsumer", deliverCallback, cancelCallback)
                this.channel = channel
            }
        }
    }

    override fun sendToComponent(payload: String, to: String) {
        println("Send $payload to Behaviour component")
    }

    override fun receiveFromComponent(from: String): String {
        return "Fake state"
    }

    override suspend fun cycle() {
        val r = receiveFromComponent("")
        myCommunication.send(r)
        val rr = myCommunication.receive()
        sendToComponent(rr, "")
    }
}
