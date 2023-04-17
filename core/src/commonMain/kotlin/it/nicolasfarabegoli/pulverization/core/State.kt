package it.nicolasfarabegoli.pulverization.core

import it.nicolasfarabegoli.pulverization.dsl.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.model.State

/**
 * Marker interface modelling the representation of the [State].
 * Each state representation must implement this interface.
 */
// interface StateRepresentation

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
