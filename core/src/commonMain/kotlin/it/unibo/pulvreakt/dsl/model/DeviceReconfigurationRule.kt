package it.unibo.pulvreakt.dsl.model

import arrow.core.NonEmptySet
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.core.reconfiguration.event.ReconfigurationEvent

data class NewConfiguration(val component: ComponentType, val destinationHost: Host)

data class DeviceReconfigurationRule(val event: ReconfigurationEvent<*>, val newConfiguration: NewConfiguration)
typealias OnDeviceRules = Set<DeviceReconfigurationRule>

typealias ComponentStartupHost = Map<Component, Host>

data class ReconfigurationRules(val onDeviceRules: OnDeviceRules?)

data class DeviceRuntimeConfiguration(
    val deviceName: String,
    val componentStartupHost: ComponentStartupHost,
    val reconfigurationRules: ReconfigurationRules?,
)

typealias ConfiguredDevicesRuntimeConfiguration = NonEmptySet<DeviceRuntimeConfiguration>
