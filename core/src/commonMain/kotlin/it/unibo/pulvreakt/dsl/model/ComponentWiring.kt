package it.unibo.pulvreakt.dsl.model

import arrow.core.NonEmptySet
import it.unibo.pulvreakt.api.component.ComponentRef

typealias ComponentsGraph = Map<ComponentRef, Set<ComponentRef>>
typealias RequiredCapabilities = Map<ComponentRef, NonEmptySet<Capability>>

/**
 * Represents the structure of a device characterized by a [deviceName],
 * a [componentsGraph] that describes the links between the components of the device,
 * and the [requiredCapabilities] of each component.
 */
data class DeviceStructure(
    val deviceName: String,
    val componentsGraph: ComponentsGraph,
    val requiredCapabilities: RequiredCapabilities,
)

typealias ConfiguredDeviceStructure = NonEmptySet<DeviceStructure>

/**
 * Extension function to get a [DeviceStructure] from a [ConfiguredDeviceStructure] by its [deviceName].
 */
operator fun ConfiguredDeviceStructure.get(deviceName: String): DeviceStructure? = find { it.deviceName == deviceName }
