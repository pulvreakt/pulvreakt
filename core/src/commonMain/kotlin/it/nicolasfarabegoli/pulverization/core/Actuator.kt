package it.nicolasfarabegoli.pulverization.core

import kotlin.reflect.KClass

/**
 * Model the concept of a single [Actuator] in the pulverization context.
 * An [Actuator] can [actuate] an operation over the environment.
 * @param T the type of the payload to send to the [Actuator]
 */
interface Actuator<in T> : Initializable {
    /**
     * The operation of actuate an action over the environment.
     */
    suspend fun actuate(payload: T)
}

/**
 * Model the concept of set of [Actuator]s in the pulverization context.
 * Contains a set of [Actuator] managed by a single [Device].
 * @param I the type of the ID of each [Actuator].
 */
abstract class ActuatorsContainer : PulverizedComponent<Any, Any, Any, Any, Any> {

    override val componentType: PulverizedComponentType = ActuatorsComponent

    /**
     * The collection of [Actuator]s.
     */
    private var actuators: Set<Actuator<*>> = emptySet()

    /**
     * Add an [actuator] to the [ActuatorsContainer].
     */
    operator fun <P, A : Actuator<P>> plusAssign(actuator: A) {
        actuators = actuators + actuator
    }

    /**
     * Add [allActuators] to the [ActuatorsContainer].
     */
    fun <A : Actuator<*>> addAll(vararg allActuators: A) {
        actuators = actuators + allActuators.toSet()
    }

    /**
     * Returns a single [Actuator] of the given [type].
     * This method should be called when a single instance of the specific [type] is available in the container,
     * otherwise a single instance is taken.
     * If no [Actuator] of the given [type] is available, null is returned.
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <T, A : Actuator<T>> get(type: KClass<A>): A? =
        actuators.firstOrNull { type.isInstance(it) } as? A

    /**
     * Returns a set of [Actuator]s of a certain [type].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T, A : Actuator<T>> getAll(type: KClass<in A>): Set<A> =
        actuators.mapNotNull { e -> e.takeIf { type.isInstance(it) } as? A }.toSet()

    /**
     * Returns a single [Actuator] of the type [A].
     * This method should be called when a single instance of the specific type [A] is available in the container,
     * otherwise a single instance is taken.
     * If no [Actuator] of the given type [A] is available, null is returned.
     */
    inline fun <reified A : Actuator<*>> get(): A? = this[A::class]

    /**
     * Finds a single [Actuator] of the type [A] and make it available inside the [run] function scope.
     * This method should be called when a single instance of the specific type [A] is available in the container,
     * otherwise a single instance is taken.
     * If no [Actuator] of the given type [A] is available, the [run] function is not executed.
     */
    inline fun <reified A : Actuator<*>> get(run: A.() -> Unit) = this[A::class]?.run()

    /**
     * Returns a set of [Actuator]s of type [A].
     */
    inline fun <reified A : Actuator<*>> getAll(): Set<A> = getAll(A::class)

    /**
     * Finds all [Actuator]s of the type [A] and make it available as a [Set] inside the [run] function scope.
     */
    inline fun <reified A : Actuator<*>> getAll(run: Set<A>.() -> Unit) = getAll(A::class).run()
}
