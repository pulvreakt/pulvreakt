package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config

import it.nicolasfarabegoli.pulverization.core.DeviceID

/**
 * TODO.
 */
actual fun <I : DeviceID> pulverizationSetup(
    deviceName: I,
    config: RabbitmqPulverizationConfig,
    init: RabbitmqPulverizationSetup.() -> Unit,
): Unit = TODO()
