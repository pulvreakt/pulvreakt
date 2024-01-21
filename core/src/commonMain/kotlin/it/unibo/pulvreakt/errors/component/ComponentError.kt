package it.unibo.pulvreakt.errors.component

import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.errors.communication.ChannelError

/**
 * Represents the possible errors that can occur during the execution of a [Component].
 */
sealed interface ComponentError {
    /**
     * Represents the error that occurs when the wiring of the components is not initialized.
     */
    data object WiringNotInitialized : ComponentError

    /**
     * Represents the error that occurs when the injector is not initialized.
     */
    data object ContextNotInitialized : ComponentError

    /**
     * Represents the error that occurs when the component is finalized before the initialization.
     */
    data object FinalizedBeforeInitialization : ComponentError

    /**
     * Represents the error that occurs when the component is trying to be used before the initialization.
     */
    data object ComponentNotInitialized : ComponentError

    /**
     * Represents the error that occurs when try to communicate with a [component] that is not registered.
     */
    data class ComponentNotRegistered(val component: ComponentRef) : ComponentError

    /**
     * Represents a generic error that occurs during the execution of the component.
     * The error is represented by a [message].
     */
    data class ExecutionError(val message: String) : ComponentError

    /**
     * Rethrow the [error] raised by the [Channel] in the context of the [Component].
     * This is used to propagate the underling [ChannelError] to the [Component].
     */
    data class WrapCommunicatorError(val error: ChannelError) : ComponentError
}
