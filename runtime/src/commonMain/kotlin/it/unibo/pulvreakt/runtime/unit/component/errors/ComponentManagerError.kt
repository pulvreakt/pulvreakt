package it.unibo.pulvreakt.runtime.unit.component.errors

import it.unibo.pulvreakt.core.component.ComponentRef

sealed interface ComponentManagerError

data class ComponentNotRegistered(val component: ComponentRef) : ComponentManagerError
