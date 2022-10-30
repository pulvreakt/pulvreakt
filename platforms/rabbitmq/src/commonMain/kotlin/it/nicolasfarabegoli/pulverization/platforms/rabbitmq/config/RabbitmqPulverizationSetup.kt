package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config

import it.nicolasfarabegoli.pulverization.core.DeviceIDOps.toID
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
fun pulverizationSetup(init: RabbitmqPulverizationSetup.() -> Unit) {
    val s = RabbitmqPulverizationSetup().apply(init)
    s.koinModule.single { RabbitmqContext("1".toID()) } // TODO: Use environment variable
    startKoin {
        modules(s.koinModule)
    }
}
