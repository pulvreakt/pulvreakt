package it.unibo.pulvreakt.dsl.deployment.model

import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.reconfiguration.Reconfigurator

/**
 * The runtime configuration belonging to a logical device.
 * Hold information about the current registered [components] (and their startup hosts).
 * Also holds a [communicatorProvider] that can be used to create a [Communicator] instance.
 * Also holds a [reconfiguratorProvider] that can be used to create a [Reconfigurator] instance.
 */
data class RuntimeSpecification(
    val components: ComponentsContainer,
    val communicatorProvider: () -> Communicator,
    val reconfiguratorProvider: () -> Reconfigurator,
)
