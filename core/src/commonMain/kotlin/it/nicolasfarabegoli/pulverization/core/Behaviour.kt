package it.nicolasfarabegoli.pulverization.core

/**
 * Represents the output of the function modelling the behaviour.
 * Contains the [newState], the [newExport], the [actuations] and the function [outcome].
 * @param S the type of the state
 * @param E the type of the export
 * @param A the type of the actuations
 * @param O the type of the outcome of the function
 */
data class BehaviourOutput<S : Any, E : Any, A : Any, O : Any>(
    val newState: S,
    val newExport: E,
    val actuations: A,
    val outcome: O,
)

/**
 * Models a _behaviour_ in the pulverization context.
 * A [Behaviour] is a pure function of the kind `B(x, e, o) = (x', e', a)`
 * @param S the type of the state
 * @param E the type of the export
 * @param W the type of the sensed values
 * @param A the type of the actuation to do
 */
interface Behaviour<S : Any, E : Any, W : Any, A : Any, O : Any> : PulverizedComponent<S, E, W, A, O> {
    override val componentType: PulverizedComponentType
        get() = BehaviourComponent

    /**
     * The function used to implement the device logic.
     */
    operator fun invoke(state: S, export: List<E>, sensedValues: W): BehaviourOutput<S, E, A, O>
}
