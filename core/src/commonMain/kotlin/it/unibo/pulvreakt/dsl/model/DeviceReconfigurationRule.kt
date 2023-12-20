package it.unibo.pulvreakt.dsl.model

import arrow.core.NonEmptySet
import it.unibo.pulvreakt.api.component.Component
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.infrastructure.Host
import it.unibo.pulvreakt.api.reconfiguration.event.ReconfigurationEvent

/**
 * Models a new configuration in the system.
 * The [component] is moved to the [destinationHost].
 */
data class NewConfiguration(val component: ComponentRef, val destinationHost: Host)

/**
 * Models a reconfiguration rule for a device.
 * The [event] triggers the [newConfiguration] of the device.
 */
data class DeviceReconfigurationRule(val event: ReconfigurationEvent<*>, val newConfiguration: NewConfiguration)
typealias OnDeviceRules = Set<DeviceReconfigurationRule>

typealias ComponentStartupHost = Map<Component, Host>

/**
 * Models the reconfiguration rules of the device.
 * [onDeviceRules] are the rules that are triggered by events that occur on the device.
 * [onDeviceRules] will be `null` if no reconfiguration rules are specified for the device.
 */
data class ReconfigurationRules(val onDeviceRules: OnDeviceRules?)

/**
 * Models the runtime configuration of a device.
 * [deviceName] is the name of the device.
 * [componentStartupHost] is the map that associates a component to the host on which it is started.
 * [reconfigurationRules] are the reconfiguration rules of the device.
 */
data class DeviceRuntimeConfiguration(
    val deviceName: String,
    val componentStartupHost: ComponentStartupHost,
    val reconfigurationRules: ReconfigurationRules?,
)

typealias ConfiguredDevicesRuntimeConfiguration = NonEmptySet<DeviceRuntimeConfiguration>
