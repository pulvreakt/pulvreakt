package it.unibo.pulvreakt.core.component

/**
 * Represents the type of a component referenced by the [ComponentRef].
 */
sealed interface ComponentType {
    /**
     * Beheaviour component.
     */
    object Behaviour : ComponentType

    /**
     * State component.
     */
    object State : ComponentType

    /**
     * Communication component.
     */
    object Communication : ComponentType

    /**
     * Sensor component.
     */
    object Sensor : ComponentType

    /**
     * Actuator component.
     */
    object Actuator : ComponentType

    /**
     * Generic component.
     */
    object Generic : ComponentType
}

/**
 * Symbolic reference to a component.
 */
interface ComponentRef {
    /**
     * The [name] of the symbolic reference.
     */
    val name: String

    /**
     * The [type] of the component referenced.
     */
    val type: ComponentType

    companion object {
        /**
         * Creates a [ComponentRef] from a [component], with optionally the [type] of the component (by default set to [ComponentType.Generic]).
         */
        fun create(component: Component, type: ComponentType = ComponentType.Generic): ComponentRef =
            ComponentRefImpl(component::class.simpleName!!, type)

        /**
         * Creates a [ComponentRef] from a [C] component, with optionally the [type] of the component (by default set to [ComponentType.Generic]).
         */
        inline fun <reified C : Component> create(type: ComponentType = ComponentType.Generic): ComponentRef =
            ComponentRefImpl(C::class.simpleName!!, type)
    }
}

@PublishedApi
internal data class ComponentRefImpl(override val name: String, override val type: ComponentType) : ComponentRef
