package it.nicolasfarabegoli.pulverization.example01 // ktlint-disable filename

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun main() = runBlocking {
    val appModule = module {
        single { MyState() }
        single { MyBehaviour() }
    }

    startKoin {
        modules(appModule)
    }

    val component = MyBehaviourComponent("1")

    // Smart logic to handle the component lifecycle.
    repeat(5) {
        component.cycle()
        delay(1000)
    }
}
