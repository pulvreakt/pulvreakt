package it.nicolasfarabegoli.pulverization.core

import kotlin.reflect.KClass

/** Models the concept of single [Sensor] in the pulverization context.
 * A [Sensor] can [sense] the environment by measuring one of its magnitudes.
 * @param T the type of the measuring after the [sense] operation.
 * @param I the identifier of the sensor.
 */
interface Sensor<out T, I> {
    val id: I
    fun sense(): T
}

/**
 * Model the concept of set of [Sensor]s in the pulverization context.
 * Contains a set of [Sensor]s managed by a single [Device].
 * @param I the type of the ID of each [Sensor].
 */
class SensorsContainer<I> {
    /**
     * The set of [Sensor]s.
     */
    private var sensors: Set<Sensor<*, I>> = emptySet()

    /**
     * Add a [Sensor] to the [SensorsContainer].
     */
    operator fun <P, S : Sensor<P, I>> plusAssign(sensor: S) {
        sensors = sensors + sensor
    }

    /**
     * Add multiple [Sensor] to the [SensorsContainer].
     */
    fun <P, S : Sensor<P, I>> addAll(vararg allSensor: S) {
        sensors = sensors + allSensor.toSet()
    }

    /**
     * Returns a single [Sensor] of the given [type]. This method should be called when a single
     * instance of the specific [type] is available in the container, otherwise a single instance is taken.
     * If no [Sensor] of the given [type] is available, null is returned.
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <T, S : Sensor<T, I>> get(type: KClass<S>): S? =
        sensors.firstOrNull(type::isInstance) as? S

    /**
     * Returns a set of [Sensor] of the given [type].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T, S : Sensor<T, I>> getAll(type: KClass<S>): Set<S> =
        sensors.mapNotNull { e -> e.takeIf { type.isInstance(it) } as? S }.toSet()

    /**
     * Returns a single [Sensor] of the type [S].
     * This method should be called when a single instance of the specific type [S] is available in the container,
     * otherwise a single instance is taken.
     * If no [Actuator] of the given type [S] is available, null is returned.
     */
    inline fun <reified S : Sensor<*, I>> get(): S? = this[S::class]

    /**
     * Finds a single [Sensor] of the type [S] and make it available inside the [run] function scope.
     * This method should be called when a single instance of the specific type [S] is available in the container,
     * otherwise a single instance is taken.
     * If no [Sensor] of the given type [S] is available, the [run] function is not executed.
     */
    inline fun <reified S : Sensor<*, I>> get(run: S.() -> Unit) = this[S::class]?.run()

    /**
     * Returns a set of [Sensor]s of type [S].
     */
    inline fun <reified S : Sensor<*, I>> getAll(): Set<S> = getAll(S::class)

    /**
     * Finds all [Sensor]s of the type [S] and make it available as a [Set] inside the [run] function scope.
     */
    inline fun <reified S : Sensor<*, I>> getAll(run: Set<S>.() -> Unit) = getAll(S::class).run()
}
