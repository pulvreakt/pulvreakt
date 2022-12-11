package example.units

import example.components.DeviceSensors
import example.components.GpsSensor
import example.components.LocalizationSensor
import it.nicolasfarabegoli.pulverization.dsl.getDeviceConfiguration
import it.nicolasfarabegoli.pulverization.runtime.dsl.PulverizationPlatformScope.Companion.sensorsLogic
import it.nicolasfarabegoli.pulverization.runtime.dsl.pulverizationPlatform
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

fun main() = runBlocking {
    val platform = pulverizationPlatform(config.getDeviceConfiguration("gps")!!) {
        sensorsLogic(LocalizationSensor()) { sensors, behaviour ->
            sensors.get<GpsSensor> {
                repeat(10) {
                    val payload = DeviceSensors(sense())
                    behaviour.sendToComponent(payload)
                    delay(2.seconds)
                }
            }
        }
    }
    val jobs = platform.start()
    jobs.forEach { it.join() }
    platform.stop()
}
