package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.core.Sensor

class MySensor(override val id: String) : Sensor<Double, String> {
    override fun sense(): Double = 55.7
}

sealed class SensorPayload {
    data class SensorResult(val sensorId: String, val value: Double) : SensorPayload()
}
