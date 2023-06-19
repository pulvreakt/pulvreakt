package it.unibo.pulvreakt.dsl.model

import arrow.core.NonEmptySet

typealias ComponentName = String
typealias ComponentsGraph = Map<ComponentName, Set<ComponentName>>
typealias RequiredCapabilities = Map<ComponentName, Set<Capability>>

data class DeviceStructure(
    val deviceName: String,
    val componentsGraph: ComponentsGraph,
    val requiredCapabilities: RequiredCapabilities,
)

typealias ConfiguredDeviceStructure = NonEmptySet<DeviceStructure>
