package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.DeviceID

/**
 * Representation of the RabbitMQ-specific context.
 */
data class RabbitmqContext(
    override val id: DeviceID,
) : Context
