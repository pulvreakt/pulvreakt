package it.nicolasfarabegoli.pulverization.example01 // ktlint-disable filename

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun main() = runBlocking {
    val deviceID = System.getenv()["DEVICE_ID"] ?: "1"
    val config = Config { addSpec(PulverizationConfig) }
        .from.yaml.resource("pulverization.yaml")

    val mySensor = MySensor("sensor-1-$deviceID")

    val module = module {
        single { SensorsContainer<String>().apply { this += mySensor } }
        single { config }
    }

    startKoin { modules(module) }

    val component = MySensorsComponent(deviceID)

    repeat(config[PulverizationConfig.repetitions]) {
        component.cycle()
        delay(1000)
    }

    component.finalize()
}
