package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config

import it.nicolasfarabegoli.pulverization.config.PulverizationScope
import it.nicolasfarabegoli.pulverization.config.Topology
import it.nicolasfarabegoli.pulverization.core.DeviceID

/**
 * Represents the rabbitmq broker configuration.
 * The configuration holds the [hostname] to connect to and the relative [port].
 */
data class BrokerConfiguration(var hostname: String = "rabbitmq", var port: Int = 5672)

/**
 * represent the final configuration holding the [topology] and the [brokerConfiguration].
 */
data class RabbitmqPulverizationConfig<I : DeviceID>(
    val topology: Topology<I>,
    val brokerConfiguration: BrokerConfiguration,
)

/**
 * Scope for configure rabbitmq.
 */
class RabbitmqScope<I : DeviceID> : PulverizationScope<I>() {
    /**
     * The broker configuration.
     */
    var brokerConfig = BrokerConfiguration()

    /**
     * Entrypoint for configure the rabbitmq parameters.
     */
    fun rabbitmq(init: BrokerConfiguration.() -> Unit) {
        brokerConfig.apply(init)
    }
}

/**
 * Function for configure the pulverization platform using rabbitmq as lowe infrastructure.
 */
fun <I : DeviceID> pulverizationConfiguration(init: RabbitmqScope<I>.() -> Unit): RabbitmqPulverizationConfig<I> {
    val config = RabbitmqScope<I>().apply(init)
    return RabbitmqPulverizationConfig(config.topology, config.brokerConfig)
}
