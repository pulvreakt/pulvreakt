package example.components

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Sensor
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

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

suspend fun mySensorsLogic(sensors: SensorsContainer, behaviour: BehaviourRef<DeviceSensors>) {
    sensors.get<GpsSensor> {
        repeat(10) {
            val payload = DeviceSensors(sense())
            behaviour.sendToComponent(payload)
            delay(2.seconds)
        }
    }
}
