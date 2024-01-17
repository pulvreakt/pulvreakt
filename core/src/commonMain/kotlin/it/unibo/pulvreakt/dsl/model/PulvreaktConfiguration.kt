package it.unibo.pulvreakt.dsl.model

import it.unibo.pulvreakt.api.communication.protocol.Protocol

typealias DevicesConfiguration<ID> = Map<String, DeviceSpecification<ID>>

/**
 * Represents the container of configurations and additional information for the communication.
 * [devicesConfiguration] is the map that associates a device name to its specification.
 * [protocol] is the [Protocol] that will be used for the communication.
 */
data class PulvreaktConfiguration<ID : Any>(
    val devicesConfiguration: DevicesConfiguration<ID>,
    val protocol: Protocol,
) {
    /**
     * Returns the device specification of the device with the given [deviceName].
     */
    operator fun get(deviceName: String): DeviceSpecification<ID>? = devicesConfiguration[deviceName]
}
