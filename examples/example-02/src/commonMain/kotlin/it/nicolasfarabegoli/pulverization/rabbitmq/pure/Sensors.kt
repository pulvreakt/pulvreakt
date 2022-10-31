package it.nicolasfarabegoli.pulverization.rabbitmq.pure

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Sensor
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import kotlinx.serialization.Serializable
import org.koin.core.component.inject
import kotlin.random.Random

typealias SensorPayload = Double

@Serializable
data class AllSensorsPayload(val deviceSensor: SensorPayload)

class DeviceSensor : Sensor<SensorPayload> {
    override fun sense(): SensorPayload = Random.nextDouble(0.0, 100.0)
}

class DeviceSensorsContainer : SensorsContainer() {
    override val context: Context by inject()
}
