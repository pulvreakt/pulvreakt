package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config

import com.rabbitmq.client.Address
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import it.nicolasfarabegoli.pulverization.core.DeviceID
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import org.koin.core.context.startKoin

/**
 * TODO.
 */
actual fun <I : DeviceID> pulverizationSetup(
    deviceName: I,
    config: RabbitmqPulverizationConfig,
    init: RabbitmqPulverizationSetup.() -> Unit,
) {
    val setup = RabbitmqPulverizationSetup().apply(init)
    val cf = ConnectionFactory()
    cf.useNio()
    val connection = cf.newConnection { listOf(Address(config.hostname, config.port)) }
    setup.koinModule.single<Connection> { connection }
    setup.koinModule.single { RabbitmqContext(deviceName) }
    startKoin {
        modules(setup.koinModule)
    }
}
