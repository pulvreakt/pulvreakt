package it.nicolasfarabegoli.pulverization.core

import kotlin.reflect.KClass

/**
 * Models the concept of payload given by a [Sensor] after a measuring operation.
 * ```kotlin
 * data class TemperaturePayload(temp: Double): SensorPayload
 * ```
 */
interface SensorPayload

/** Models the concept of single [Sensor] in the pulverization context.
 * A [Sensor] can [sense] the environment by measuring one of its magnitudes.
 * @param T the type of the measuring after the [sense] operation.
 * @param I the identifier of the sensor.
 */
interface Sensor<out T : SensorPayload, I> {
    val id: I
    fun sense(): T
}

/**
 * Model the concept of set of [Sensor]s in the pulverization context.
 * Contains a set of [Sensor]s managed by a single [Device].
 * @param I the type of the ID of each [Sensor].
 */
interface SensorsContainer<I> {
    /**
     * The set of [Sensor]s.
     */
    var sensors: Set<Sensor<SensorPayload, I>>

    /**
     * Add a [Sensor] to the [SensorsContainer].
     */
    fun addSensor(sensor: Sensor<SensorPayload, I>)

    /**
     * Returns a single [Sensor] of the given [type]. This method should be called when a single
     * instance of the specific [type] is available in the container, otherwise a single instance is taken.
     * If no [Sensor] of the given [type] is available, null is returned.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : SensorPayload, S : Sensor<T, I>> getSensor(type: KClass<S>): S? =
        sensors.firstOrNull(type::isInstance) as? S

    /**
     * Returns a set of [Sensor] of the given [type].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : SensorPayload, S : Sensor<T, I>> getSensors(type: KClass<S>): Set<S> =
        sensors.mapNotNull { e -> e.takeIf { type.isInstance(it) } as? S }.toSet()

    companion object {
        inline fun <I, T : SensorPayload, reified S : Sensor<T, I>> SensorsContainer<I>.getSensor(): S? =
            sensors.filterIsInstance<S>().firstOrNull()

        inline fun<I, T : SensorPayload, reified S : Sensor<T, I>> SensorsContainer<I>.getSensors(): Set<S> =
            sensors.filterIsInstance<S>().toSet()
    }
}
