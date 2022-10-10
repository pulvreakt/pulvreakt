package it.nicolasfarabegoli.pulverization.example01 // ktlint-disable filename

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

fun main() = runBlocking {
    val deviceID = System.getenv()["DEVICE_ID"] ?: "1"
    val config = Config { addSpec(PulverizationConfig) }
        .from.yaml.resource("pulverization.yaml")

    val appModule = module {
        single { config }
        single { MyState("state -> $deviceID") }
        single { MyBehaviour(deviceID) }
    }

    startKoin {
        modules(appModule)
    }

    val component = MyBehaviourComponent(deviceID)

    // Smart logic to handle the component lifecycle.
    repeat(config[PulverizationConfig.repetitions]) {
        component.cycle()
        delay(1000)
    }
    component.finalize()
}
