package it.unibo.pulvreakt.runtime.errors

import it.unibo.pulvreakt.errors.protocol.ProtocolError
import it.unibo.pulvreakt.runtime.reconfigurator.UnitReconfigurator
import it.unibo.pulvreakt.runtime.reconfigurator.errors.UnitReconfiguratorError
import it.unibo.pulvreakt.runtime.unit.UnitManager
import it.unibo.pulvreakt.runtime.unit.errors.UnitManagerError

/**
 * Represents an error that can occur during the runtime.
 */
sealed interface RuntimeError

/**
 * Represents an error when the configuration for a [device] is not found.
 */
data class DeviceConfigurationNotFound(val device: String) : RuntimeError

/**
 * Represents an [error] that can occur during the configuration of a unit.
 */
data class WrapUnitManagerError(val error: UnitManagerError) : RuntimeError

/**
 * Represents an [error] that can occur during the reconfiguration of a unit.
 */
data class WrapUnitReconfiguratorError(val error: UnitReconfiguratorError) : RuntimeError

/**
 * Represents an [error] that can occur during the configuration of a protocol.
 */
data class WrapProtocolError(val error: ProtocolError) : RuntimeError

/**
 * Represents an error that can occur when the [UnitManager] is not initialized.
 */
object UnitManagerNotInitialized : RuntimeError

/**
 * Represents an error that can occur when the [UnitReconfigurator] is not initialized.
 */
object UnitReconfiguratorNotInitialized : RuntimeError
