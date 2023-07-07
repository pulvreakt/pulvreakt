package it.unibo.pulvreakt.dsl.model

/**
 * Represents the device specification.
 * It is characterized by a [deviceName],
 * a [componentsConfiguration] that describes the links between the components of the device,
 * the [requiredCapabilities] of each component,
 * and the [runtimeConfiguration] of the device.
 */
data class DeviceSpecification(
    val deviceName: String,
    val componentsConfiguration: ComponentsGraph,
    val requiredCapabilities: RequiredCapabilities,
    val runtimeConfiguration: DeviceRuntimeConfiguration,
)
