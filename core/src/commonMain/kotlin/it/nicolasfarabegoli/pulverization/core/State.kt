package it.nicolasfarabegoli.pulverization.core

/**
 * Marker interface modelling the representation of the [State].
 * Each state representation must implement this interface.
 */
interface StateRepresentation

/**
 * Models the concept of [State] in the pulverization context.
 * @param S the type of the [State].
 */
interface State<S : StateRepresentation> : PulverizedComponent {
    /**
     * Retrive the value of the [State].
     */
    fun get(): S

    /**
     * Update the [State] with a [newState].
     */
    fun update(newState: S): S
}
