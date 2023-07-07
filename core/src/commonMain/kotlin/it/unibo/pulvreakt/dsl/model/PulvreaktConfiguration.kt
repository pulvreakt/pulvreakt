package it.unibo.pulvreakt.dsl.model

import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.reconfiguration.Reconfigurator

typealias DevicesConfiguration = Map<String, DeviceSpecification>
typealias CommunicatorProvider = () -> Communicator
typealias ReconfiguratorProvider = () -> Reconfigurator

/**
 * Represents the container of configurations and additional information for the communication.
 * [devicesConfiguration] is the map that associates a device name to its specification.
 * [communicatorProvider] is the function that provides the communicator.
 * [reconfiguratorProvider] is the function that provides the reconfigurator.
 */
data class PulvreaktConfiguration(
    val devicesConfiguration: DevicesConfiguration,
    val communicatorProvider: CommunicatorProvider,
    val reconfiguratorProvider: ReconfiguratorProvider,
) {
    /**
     * Returns the device specification of the device with the given [deviceName].
     */
    operator fun get(deviceName: String): DeviceSpecification? = devicesConfiguration[deviceName]
}
