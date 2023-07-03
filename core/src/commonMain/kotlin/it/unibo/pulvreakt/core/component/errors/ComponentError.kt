package it.unibo.pulvreakt.core.component.errors

import it.unibo.pulvreakt.core.communicator.errors.CommunicatorError
import it.unibo.pulvreakt.core.component.ComponentRef

sealed interface ComponentError {
    object WiringNotInitialized : ComponentError
    object InjectorNotInitialized : ComponentError
    object FinalizedBeforeInitialization : ComponentError
    object ComponentNotInitialized : ComponentError
    data class ComponentNotRegistered(val component: ComponentRef<*>) : ComponentError
    data class ExecutionError(val message: String) : ComponentError
    data class LiftCommunicatorError(val errro: CommunicatorError) : ComponentError
}
