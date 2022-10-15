package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.SendOnlyDeviceComponent
import it.nicolasfarabegoli.pulverization.core.Sensor
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import kotlin.random.Random

class MySensor(override val id: String) : Sensor<Double, String> {
    override fun sense(): Double = Random.nextDouble(0.0, 10.0)
}

sealed class SensorPayload {
    @Serializable
    data class SensorResult(val sensorId: String, val value: Double) : SensorPayload()
}

expect class MySensorsComponent : SendOnlyDeviceComponent<SensorPayload, String>, KoinComponent
