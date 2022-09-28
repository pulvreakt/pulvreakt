package it.nicolasfarabegoli.pulverization.core

/**
 * Models the concept of [State] in the pulverization context.
 * @param S the type of the state.
 */
interface State<S> {
    /**
     * The device state.
     */
    var state: S
}
