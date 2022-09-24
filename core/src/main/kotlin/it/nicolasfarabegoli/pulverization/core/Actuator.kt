package it.nicolasfarabegoli.pulverization.core

/**
 * Models the concept of _Actuator_ in the pulverization context.
 * An [Actuator] can [actuate] an effect on the environment.
 * @param T the type of the payload needed to interact with the actuator.
 * @param I the identifier of the actuator.
 */
interface Actuator<T, I> {
    /**
     * The identifier of the actuator. The generic type allow to identify the appropriate representation for the [id].
     */
    val id: I

    /**
     * Represent the action of actuate an effect on the environment.
     */
    fun actuate(payload: T)
}
