package it.unibo.pulvreakt.runtime.component.errors

import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.runtime.component.ComponentManager

/**
 * Represents an error that can occur on the [ComponentManager].
 */
sealed interface ComponentManagerError

/**
 * Represents an error that can occur when a [component] is not registered.
 */
data class ComponentNotRegistered(val component: ComponentRef) : ComponentManagerError
