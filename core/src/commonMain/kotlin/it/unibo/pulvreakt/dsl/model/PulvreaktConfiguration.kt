package it.unibo.pulvreakt.dsl.model

import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.reconfiguration.Reconfigurator

typealias DevicesConfiguration = Map<String, DeviceSpecification>
typealias CommunicatorProvider = () -> Communicator
typealias ReconfiguratorProvider = () -> Reconfigurator

data class PulvreaktConfiguration(
    val devicesConfiguration: DevicesConfiguration,
    val communicatorProvider: CommunicatorProvider,
    val reconfiguratorProvider: ReconfiguratorProvider,
) {
    operator fun get(deviceName: String): DeviceSpecification? = devicesConfiguration[deviceName]
}
