package it.unibo.pulvreakt.core.component.pulverisation

import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.ComponentType

internal sealed interface StateOps {
    object GetState : StateOps
    data class SetState<StateRepr : Any>(val state: StateRepr) : StateOps
}

/**
 * Represents the State component in the pulverization model.
 */
abstract class State<StateRepr : Any> : AbstractComponent() {
    /**
     * Set the state of the component to the given [newState].
     */
    abstract fun setState(newState: StateRepr)

    /**
     * Get the state of the component.
     */
    abstract fun getState(): StateRepr

    final override fun getRef(): ComponentRef = ComponentRef.create(this, ComponentType.State)
}
