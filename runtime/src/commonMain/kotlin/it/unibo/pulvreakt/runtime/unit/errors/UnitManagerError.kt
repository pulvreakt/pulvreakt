package it.unibo.pulvreakt.runtime.unit.errors

import it.unibo.pulvreakt.errors.component.ComponentError
import it.unibo.pulvreakt.runtime.component.ComponentManager
import it.unibo.pulvreakt.runtime.component.errors.ComponentManagerError

/**
 * Represents an error that can occur during the management of a unit.
 */
sealed interface UnitManagerError

/**
 * Represents an [error] that can occur during the management of a component.
 */
data class WrapComponentError(val error: ComponentError) : UnitManagerError

/**
 * Represents an [error] that can occur in the [ComponentManager].
 */
data class WrapComponentManagerError(val error: ComponentManagerError) : UnitManagerError
