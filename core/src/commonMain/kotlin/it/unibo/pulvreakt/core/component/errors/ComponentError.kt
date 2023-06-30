package it.unibo.pulvreakt.core.component.errors

import it.unibo.pulvreakt.core.component.ComponentRef

sealed interface ComponentError {
    object ComponentNotInitialized : ComponentError
    object InjectorNotInitialized : ComponentError
    data class ComponentNotRegistered(val component: ComponentRef<*>) : ComponentError
    data class ExecutionError(val message: String) : ComponentError
}
