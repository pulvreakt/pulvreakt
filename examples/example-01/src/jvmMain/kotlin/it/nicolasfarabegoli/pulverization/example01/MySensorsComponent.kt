package it.nicolasfarabegoli.pulverization.example01

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.uchuhimo.konf.Config
import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MySensorsComponent(override val deviceID: String) : DeviceComponent<SensorPayload, Unit, String>, KoinComponent {
    private val sensorsContainer: SensorsContainer<String> by inject()
    private val config: Config by inject()
    private val connection: Connection
    private val channel: Channel

    init {
        val connection = ConnectionFactory().apply { host = config[PulverizationConfig.hostname]; port = config[PulverizationConfig.port] }
        connection.newConnection().let { conn ->
            this.connection = conn
            conn.createChannel().let { channel ->
                channel.queueDeclare("sensors/$deviceID", false, false, false, null)
                this.channel = channel
            }
        }
    }

    fun finalize() {
        channel.close()
        connection.close()
    }

    override fun sendToComponent(payload: SensorPayload, to: String) {
        when (payload) {
            is SensorPayload.SensorResult ->
                channel.basicPublish("", "sensors/$deviceID", null, "${payload.sensorId} - ${payload.value}".toByteArray())
        }
    }

    override fun receiveFromComponent(from: String) { } // Not used here

    override suspend fun cycle() {
        sensorsContainer.get<MySensor> {
            val sensedValue = sense()
            sendToComponent(SensorPayload.SensorResult("", sensedValue), "")
        }
    }
}
