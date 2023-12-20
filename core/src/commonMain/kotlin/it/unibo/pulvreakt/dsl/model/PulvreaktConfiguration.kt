package it.unibo.pulvreakt.dsl.model

import it.unibo.pulvreakt.api.protocol.Protocol

typealias DevicesConfiguration = Map<String, DeviceSpecification>

/**
 * Represents the container of configurations and additional information for the communication.
 * [devicesConfiguration] is the map that associates a device name to its specification.
 * [protocol] is the [Protocol] that will be used for the communication.
 */
data class PulvreaktConfiguration(
    val devicesConfiguration: DevicesConfiguration,
    val protocol: Protocol,
) {
    /**
     * Returns the device specification of the device with the given [deviceName].
     */
    operator fun get(deviceName: String): DeviceSpecification? = devicesConfiguration[deviceName]
}
