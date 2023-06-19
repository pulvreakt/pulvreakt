package it.unibo.pulvreakt.dsl.model

import arrow.core.NonEmptySet
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.core.reconfiguration.event.ReconfigurationEvent

data class NewConfiguration(val component: ComponentName, val destinationHost: Host)

data class DeviceReconfigurationRule(val event: ReconfigurationEvent<*>, val newConfiguration: NewConfiguration)

typealias ComponentStartupHost = Map<Component, Host>

data class DeviceRuntimeConfiguration(
    val deviceName: String,
    val componentStartupHost: ComponentStartupHost,
    val reconfigurationRules: Set<DeviceReconfigurationRule>,
)

typealias ConfiguredDevicesRuntimeConfiguration = NonEmptySet<DeviceRuntimeConfiguration>
