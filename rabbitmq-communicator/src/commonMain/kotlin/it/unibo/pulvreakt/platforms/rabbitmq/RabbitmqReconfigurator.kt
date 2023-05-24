package it.unibo.pulvreakt.platforms.rabbitmq

import it.unibo.pulvreakt.runtime.reconfiguration.Reconfigurator

/**
 * Implement the [Reconfigurator] interface relying on RabbitMQ as a platform for communication.
 */
expect class RabbitmqReconfigurator(
    hostname: String = "localhost",
    port: Int = 5672,
    username: String = "guest",
    password: String = "guest",
    virtualHost: String = "/",
) : Reconfigurator