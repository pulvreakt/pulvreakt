package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config

import it.nicolasfarabegoli.pulverization.core.DeviceID
import it.nicolasfarabegoli.pulverization.core.PulverizedComponent
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * TODO.
 */
class RabbitmqPulverizationSetup {
    /**
     * TODO.
     */
    val koinModule = Module()

    /**
     * TODO.
     */
    inline fun <reified C : PulverizedComponent> registerComponent(component: C?) {
        if (component == null) error("A concrete component should be pass, but null is given")
        koinModule.factory { component }
    }
}

/**
 * TODO.
 */
fun <I : DeviceID> pulverizationSetup(deviceName: I, init: RabbitmqPulverizationSetup.() -> Unit) {
    val setup = RabbitmqPulverizationSetup().apply(init)
    setup.koinModule.single { RabbitmqContext(deviceName) }
    startKoin {
        modules(setup.koinModule)
    }
}
