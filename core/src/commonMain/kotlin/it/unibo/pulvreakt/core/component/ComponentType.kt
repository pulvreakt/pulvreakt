package it.unibo.pulvreakt.core.component

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * High level representation of a component.
 * @param C the type of the component.
 * It is the companion of [Component].
 */
@Serializable
sealed interface ComponentType<C : Any>

/**
 * Utility class for creating a [ComponentType] via delegation.
 */
class ComponentTypeDelegate<C : Any>(private val kClass: KClass<C>) {
    /**
     * Returns the [ComponentType] for the given [property].
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): ComponentType<C> = ComponentTypeImpl(property.name, kClass.simpleName!!)

    @Serializable
    private data class ComponentTypeImpl<C : Any>(val name: String, val typeName: String) : ComponentType<C>

    companion object {
        /**
         * Smart constructor for [ComponentTypeDelegate] which reifies the generic type.
         */
        inline operator fun <reified C : Any> invoke(): ComponentTypeDelegate<C> = ComponentTypeDelegate(C::class)
    }
}
