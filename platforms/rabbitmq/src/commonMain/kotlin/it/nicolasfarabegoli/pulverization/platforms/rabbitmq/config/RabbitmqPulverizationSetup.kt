package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config

import it.nicolasfarabegoli.pulverization.core.DeviceID
import it.nicolasfarabegoli.pulverization.core.LogicalDevice
import it.nicolasfarabegoli.pulverization.core.PulverizedComponent
import it.nicolasfarabegoli.pulverization.core.get
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

    /**
     * TODO.
     */
    inline fun <reified C : PulverizedComponent> registerComponent(logicalDevice: LogicalDevice?) {
        registerComponent(logicalDevice?.get<C>())
    }
}

/**
 * TODO.
 */
expect fun <I : DeviceID> pulverizationSetup(
    deviceName: I,
    config: RabbitmqPulverizationConfig,
    init: RabbitmqPulverizationSetup.() -> Unit,
)
