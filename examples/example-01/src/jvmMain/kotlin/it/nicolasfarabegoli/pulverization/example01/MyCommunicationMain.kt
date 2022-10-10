package it.nicolasfarabegoli.pulverization.example01 // ktlint-disable filename

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun main() = runBlocking {
    val deviceID = System.getenv()["DEVICE_ID"] ?: "1"
    val config = Config { addSpec(PulverizationConfig) }
        .from.yaml.resource("pulverization.yaml")

    val devices = config[PulverizationConfig.devices].find { it.id == deviceID } ?: throw IllegalStateException()

    startKoin {
        modules(
            module {
                single { MyCommunication(deviceID, devices.neighbours.toList()) }
                single { config }
            },
        )
    }

    val component = MyCommunicationComponent(deviceID)

    repeat(config[PulverizationConfig.repetitions]) {
        component.cycle()
        delay(1000)
    }
    component.finalize()
}
