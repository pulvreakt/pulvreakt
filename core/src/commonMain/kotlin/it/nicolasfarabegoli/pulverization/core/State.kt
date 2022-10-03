package it.nicolasfarabegoli.pulverization.core

/**
 * Models the concept of [State] in the pulverization context.
 * @param S the type of the [State].
 */
interface State<S> {
    /**
     * Retrive the value of the [State].
     */
    fun get(): S

    /**
     * Update the [State] with a [newState].
     */
    fun update(newState: S): S
}
