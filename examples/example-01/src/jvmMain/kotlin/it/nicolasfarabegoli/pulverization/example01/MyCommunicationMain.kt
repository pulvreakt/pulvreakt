package it.nicolasfarabegoli.pulverization.example01

import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun main() = runBlocking {
    val appModule = module {
        single { MyCommunication() }
    }

    startKoin {
        modules(appModule)
    }

    val component = MyCommunicationComponent()

    repeat(5) {
        component.cycle()
    }
}
