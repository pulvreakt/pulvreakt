package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.SendOnlyDeviceComponent
import it.nicolasfarabegoli.pulverization.core.Sensor
import org.koin.core.component.KoinComponent

class MySensor(override val id: String) : Sensor<Double, String> {
    override fun sense(): Double = 55.7
}

sealed class SensorPayload {
    data class SensorResult(val sensorId: String, val value: Double) : SensorPayload()
}

expect class MySensorsComponent : SendOnlyDeviceComponent<SensorPayload, String>, KoinComponent
