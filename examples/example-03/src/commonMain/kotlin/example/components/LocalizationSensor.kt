package example.components

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Sensor
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import kotlinx.serialization.Serializable
import org.koin.core.component.inject

@Serializable
data class Gps(val long: Double, val lat: Double)

@Serializable
data class DeviceSensors(val gps: Gps)

class GpsSensor : Sensor<Gps> {
    override fun sense(): Gps {
        TODO("Not yet implemented")
    }
}

class LocalizationSensor : SensorsContainer() {
    override val context: Context by inject()

    override suspend fun initialize() {
        this += GpsSensor()
    }
}
