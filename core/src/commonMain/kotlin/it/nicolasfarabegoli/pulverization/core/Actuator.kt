package it.nicolasfarabegoli.pulverization.core

import kotlin.reflect.KClass

/**
 * Models the concept of payload to send to an [Actuator] in order to trigger an action on the environment.
 * A simple declaration of an [ActuatorPayload].
 * ```kotlin
 * data class ValvePayload(opening: Double) : ActuatorPayload
 * ```
 */
interface ActuatorPayload

/**
 * Model the concept of a single [Actuator] in the pulverization context.
 * @param T the type of the payload to send to the [Actuator]
 */
interface Actuator<in T : ActuatorPayload> {
    fun actuate(payload: T)
}

/**
 * Model the concept of set of [Actuator]s in the pulverization context.
 * Contains a collection of [Actuator] managed by a single [Device].
 */
interface ActuatorsContainer {
    /**
     * The collection of [Actuator]s.
     */
    var actuators: Set<Actuator<ActuatorPayload>>

    /**
     * Add an [actuator] to the [ActuatorsContainer].
     */
    fun <A : Actuator<ActuatorPayload>> addActuator(actuator: A)

    /**
     * Returns a single [Actuator] of the given [type].
     * This method should be called when a single instance of the specific [type] is available in the container,
     * otherwise a single instance is taken.
     * If no [Actuator] of the given [type] is available, null is returned.
     */
    fun <T : ActuatorPayload, A : Actuator<T>> getActuator(type: Class<A>): A? = getActuator(type.kotlin)

    /**
     * Returns a single [Actuator] of the given [type].
     * This method should be called when a single instance of the specific [type] is available in the container,
     * otherwise a single instance is taken.
     * If no [Actuator] of the given [type] is available, null is returned.
     */
    fun <T : ActuatorPayload, A : Actuator<T>> getActuator(type: KClass<A>): A? =
        actuators.filterIsInstance(type.java).firstOrNull()

    /**
     * Returns a set of [Actuator]s of a certain [type].
     */
    fun <T : ActuatorPayload, A : Actuator<T>> getActuators(type: Class<A>): Set<A> = getActuators(type.kotlin)

    /**
     * Returns a set of [Actuator]s of a certain [type].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ActuatorPayload, A : Actuator<T>> getActuators(type: KClass<in A>): Set<A> =
        actuators.filterIsInstance(type.java).toSet() as Set<A>

    companion object {
        /**
         * Returns a set of [Actuator]s of type [A] with a payload [T].
         */
        inline fun <T : ActuatorPayload, reified A : Actuator<T>> ActuatorsContainer.getActuators(): Set<A> =
            actuators.filterIsInstance<A>().toSet()

        /**
         * Returns a single [Actuator] of the type [A] with payload type [T].
         * This method should be called when a single instance of the specific type [A] is available in the container,
         * otherwise a single instance is taken.
         * If no [Actuator] of the given type [A] is available, null is returned.
         */
        inline fun <T : ActuatorPayload, reified A : Actuator<T>> ActuatorsContainer.getActuator(): A? =
            actuators.filterIsInstance<A>().firstOrNull()
    }
}
