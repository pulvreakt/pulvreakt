package it.unibo.pulvreakt.dsl.deployment.model

import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.core.reconfiguration.Reconfigurator
import it.unibo.pulvreakt.dsl.system.model.SystemSpecification

/**
 * The runtime configuration belonging to a logical device.
 * Hold information about the current registered [components] (and their startup hosts).
 * Also holds a [systemSpecification] companion of the runtime specification.
 * Also holds a [reconfgiurationRules] that can be used to reconfigure the device.
 * Also holds a [communicatorProvider] that can be used to create a [Communicator] instance.
 * Also holds a [reconfiguratorProvider] that can be used to create a [Reconfigurator] instance.
 * Also holds the [availableHosts] in the infrastructures.
 */
data class DeploymentSpecification(
    val systemSpecification: SystemSpecification,
    val components: ComponentsContainer,
    val reconfgiurationRules: ReconfigurationRules?,
    val communicatorProvider: () -> Communicator,
    val reconfiguratorProvider: () -> Reconfigurator,
    val availableHosts: Set<Host>,
)
