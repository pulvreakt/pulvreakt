package it.nicolasfarabegoli.pulverization.rabbitmq.components

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.SimpleRabbitmqSenderCommunicator
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.AllSensorsPayload
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceSensor
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceSensorsContainer
import org.koin.core.component.inject

class DeviceSensorsComponent : DeviceComponent<RabbitmqContext> {
    override val context: RabbitmqContext by inject()

    private val sensorCommunicator = SimpleRabbitmqSenderCommunicator<AllSensorsPayload>(
        SensorsComponent to BehaviourComponent,
    )

    private val sensor: DeviceSensorsContainer by inject()

    init {
        sensor += DeviceSensor()
    }

    override suspend fun initialize() {
        sensorCommunicator.initialize()
    }

    override suspend fun cycle() {
        sensor.get<DeviceSensor> {
            val payload = AllSensorsPayload(this.sense())
            sensorCommunicator.sendToComponent(payload)
        }
    }
}
