package it.nicolasfarabegoli.pulverization.core

/**
 * Models the concept of _Sensor_ in the pulverization context.
 * A [Sensor] can [sense] the environment by measuring one of its magnitudes.
 * @param S the type of the measuring after the [sense] operation.
 * @param I the identifier of the sensor.
 */
interface Sensor<S, I> {
    /**
     * The identifier of the sensor. The generic type allow to identify the appropriate representation for the [id].
     */
    val id: I

    /**
     * Represent the sensing operation over the environment.
     */
    fun sense(): S
}
