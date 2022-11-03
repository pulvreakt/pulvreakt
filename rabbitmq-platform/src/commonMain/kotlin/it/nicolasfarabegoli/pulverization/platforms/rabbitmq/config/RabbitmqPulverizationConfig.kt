package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config

import it.nicolasfarabegoli.pulverization.config.BasePulverizationConfig
import it.nicolasfarabegoli.pulverization.config.PulverizationConfigScope
import it.nicolasfarabegoli.pulverization.config.Scope
import it.nicolasfarabegoli.pulverization.core.LogicalDevice

/**
 * Extends the [BasePulverizationConfig] with rabbitmq-specific parameters.
 */
interface RabbitmqPulverizationConfig : BasePulverizationConfig {
    /**
     * The hostname of the RabbitMQ broker.
     */
    val hostname: String

    /**
     * The port of the RabbitMQ broker.
     */
    val port: Int
}

/**
 * Represents the rabbitmq broker configuration.
 * The configuration holds the [hostname] to connect to and the relative [port].
 */
data class RabbitmqPulverizationConfigImpl(
    override val devices: Map<String, LogicalDevice>,
    override val hostname: String = "rabbitmq",
    override val port: Int = 5672,
) : RabbitmqPulverizationConfig

/**
 * Scope for configuring RabbitMQ parameters.
 */
class RabbitmqScope : Scope<Pair<String, Int>> {
    private var _hostname = "rabbitmq"
    private var _port = PORT

    companion object {
        private const val PORT = 5672
    }

    /**
     * Setups the hostname.
     */
    fun setHostname(hostname: String) {
        _hostname = hostname
    }

    /**
     * Setups the port.
     */
    fun setPort(port: Int) {
        _port = port
    }

    override fun build(): Pair<String, Int> {
        return _hostname to _port
    }
}

/**
 * Scope for configuring a RabbitMQ pulverization.
 */
class RabbitmqPulverizationConfigScope : PulverizationConfigScope() {
    private var rmqConfig = RabbitmqScope()

    /**
     * Setups the RabbitMQ parameters.
     */
    fun rabbitmq(init: RabbitmqScope.() -> Unit) {
        rmqConfig.apply(init)
    }

    override fun build(): RabbitmqPulverizationConfig {
        val (hostname, port) = rmqConfig.build()
        return RabbitmqPulverizationConfigImpl(super.build().devices, hostname, port)
    }
}

/**
 * Configure the pulverization using RabbitMQ.
 */
fun pulverizationConfig(init: RabbitmqPulverizationConfigScope.() -> Unit): RabbitmqPulverizationConfig =
    RabbitmqPulverizationConfigScope().apply(init).build()

// /**
// * represent the final configuration holding the [topology] and the [brokerConfiguration].
// */
// data class RabbitmqPulverizationConfig<I : DeviceID>(
//    val topology: Topology<I>,
//    val brokerConfiguration: BrokerConfiguration,
// )
//
// /**
// * Scope for configure rabbitmq.
// */
// class RabbitmqScope<I : DeviceID> : PulverizationScope<I>() {
//    /**
//     * The broker configuration.
//     */
//    var brokerConfig = BrokerConfiguration()
//
//    /**
//     * Entrypoint for configure the rabbitmq parameters.
//     */
//    fun rabbitmq(init: BrokerConfiguration.() -> Unit) {
//        brokerConfig.apply(init)
//    }
// }
//
// /**
// * Function for configure the pulverization platform using rabbitmq as lowe infrastructure.
// */
// fun <I : DeviceID> pulverizationConfiguration(init: RabbitmqScope<I>.() -> Unit): RabbitmqPulverizationConfig<I> {
//    val config = RabbitmqScope<I>().apply(init)
//    return RabbitmqPulverizationConfig(config.topology, config.brokerConfig)
// }
