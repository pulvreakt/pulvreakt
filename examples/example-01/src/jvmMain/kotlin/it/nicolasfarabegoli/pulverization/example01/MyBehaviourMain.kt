package it.nicolasfarabegoli.pulverization.example01 // ktlint-disable filename

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

fun main() = runBlocking {
    val deviceID = System.getenv()["DEVICE_ID"] ?: "1"

    val appModule = module {
        single { MyState("state -> $deviceID") }
        single { MyBehaviour() }
    }

    startKoin {
        modules(appModule)
    }

    val component = MyBehaviourComponent(deviceID)

    // Smart logic to handle the component lifecycle.
    repeat(15) {
        component.cycle()
        delay(1000)
    }
    component.finalize()
}
