package it.unibo.pulvreakt.core

import it.unibo.pulvreakt.dsl.model.ComponentType
import it.unibo.pulvreakt.dsl.model.State

/**
 * Models the concept of [State] in the pulverization context.
 * @param S the type of the [State].
 */
interface State<S : Any> : PulverizedComponent<S, Any, Any, Any, Any> {
    override val componentType: ComponentType
        get() = State

    /**
     * Retrive the value of the [State].
     */
    fun get(): S

    /**
     * Update the [State] with a [newState].
     */
    fun update(newState: S): S
}
