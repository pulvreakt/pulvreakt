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

    val appModule = module {
        single { MyCommunication(devices.neighbours.toList()) }
    }

    startKoin {
        modules(appModule)
    }

    val component = MyCommunicationComponent(deviceID)

    repeat(15) {
        component.cycle()
        delay(1000)
    }
    component.finalize()
}
