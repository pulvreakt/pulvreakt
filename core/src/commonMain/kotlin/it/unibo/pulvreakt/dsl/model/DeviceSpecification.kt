package it.unibo.pulvreakt.dsl.model

data class DeviceSpecification(
    val deviceName: String,
    val componentsConfiguration: ComponentsGraph,
    val requiredCapabilities: RequiredCapabilities,
    val runtimeConfiguration: DeviceRuntimeConfiguration,
)
