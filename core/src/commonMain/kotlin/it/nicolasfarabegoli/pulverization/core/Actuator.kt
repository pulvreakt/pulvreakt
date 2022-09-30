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
 * An [Actuator] can [actuate] an operation over the environment.
 * @param T the type of the payload to send to the [Actuator]
 */
interface Actuator<in T : ActuatorPayload, I> {
    val id: I
    fun actuate(payload: T)
}

/**
 * Model the concept of set of [Actuator]s in the pulverization context.
 * Contains a set of [Actuator] managed by a single [Device].
 * @param I the type of the ID of each [Actuator].
 */
class ActuatorsContainer<I> {

    /**
     * The collection of [Actuator]s.
     */
    private var actuators: Set<Actuator<*, I>> = emptySet()

    /**
     * Add an [actuator] to the [ActuatorsContainer].
     */
    fun <P : ActuatorPayload, A : Actuator<P, I>> addActuator(actuator: A) {
        actuators = actuators + actuator
    }

    /**
     * Returns a single [Actuator] of the given [type].
     * This method should be called when a single instance of the specific [type] is available in the container,
     * otherwise a single instance is taken.
     * If no [Actuator] of the given [type] is available, null is returned.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ActuatorPayload, A : Actuator<T, I>> getActuator(type: KClass<A>): A? =
        actuators.firstOrNull { type.isInstance(it) } as? A

    /**
     * Returns a set of [Actuator]s of a certain [type].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ActuatorPayload, A : Actuator<T, I>> getActuators(type: KClass<in A>): Set<A> =
        actuators.mapNotNull { e -> e.takeIf { type.isInstance(it) } as? A }.toSet()

    companion object {
        /**
         * Returns a set of [Actuator]s of type [A] with a payload [T].
         */
        inline fun <I, T : ActuatorPayload, reified A : Actuator<T, I>> ActuatorsContainer<I>.getActuators(): Set<A> =
            this.getActuators(A::class)

        /**
         * Returns a single [Actuator] of the type [A] with payload type [T].
         * This method should be called when a single instance of the specific type [A] is available in the container,
         * otherwise a single instance is taken.
         * If no [Actuator] of the given type [A] is available, null is returned.
         */
        inline fun <I, T : ActuatorPayload, reified A : Actuator<T, I>> ActuatorsContainer<I>.getActuator(): A? =
            this.getActuator(A::class)
    }
}
