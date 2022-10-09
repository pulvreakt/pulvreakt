package it.nicolasfarabegoli.pulverization.example01 // ktlint-disable filename

import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun main() = runBlocking {
    val deviceID = System.getenv()["DEVICE_ID"] ?: "1"
    val mySensor = MySensor("sensor-1")

    val module = module {
        single { SensorsContainer<String>().apply { this += mySensor } }
    }

    startKoin { modules(module) }

    val component = MySensorsComponent(deviceID)

    repeat(15) {
        component.cycle()
        delay(1000)
    }
    component.finalize()
}
