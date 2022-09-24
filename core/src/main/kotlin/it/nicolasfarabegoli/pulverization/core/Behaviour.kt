package it.nicolasfarabegoli.pulverization.core

/**
 * Models a _behaviour_ in the pulverization context.
 * A [Behaviour] is a pure function of the kind `B(x, e, o) = (x', e', a)`
 * @param S the type of the state
 * @param E the type of the export
 * @param W the type of the sensed value
 * @param A the type of the actuation to do
 */
interface Behaviour<S, E, W, A> {
    fun invoke(state: S, export: E, sensedValue: W): Triple<S, E, A>
}
