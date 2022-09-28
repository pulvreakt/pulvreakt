package it.nicolasfarabegoli.pulverization.core

/**
 * Represents the output of the function modelling the behaviour.
 * Contains the [newState], the [newExport], the [actuation] and the function [outcome].
 * @param S the type of the state
 * @param E the type of the export
 * @param A the type of the actuation
 * @param O the type of the outcome of the function
 */
data class BehaviourOutput<S : State<S>, E, A, O>(val newState: S, val newExport: E, val actuation: A, val outcome: O)

/**
 * Models a _behaviour_ in the pulverization context.
 * A [Behaviour] is a pure function of the kind `B(x, e, o) = (x', e', a)`
 * @param S the type of the state
 * @param E the type of the export
 * @param W the type of the sensed value
 * @param A the type of the actuation to do
 */
interface Behaviour<S : State<S>, E, W, A, O> {
    operator fun invoke(state: S, export: E, sensedValue: W): BehaviourOutput<S, E, A, O>
}
