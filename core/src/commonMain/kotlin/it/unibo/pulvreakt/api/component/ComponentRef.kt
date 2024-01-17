package it.unibo.pulvreakt.api.component

/**
 * Kind of component.
 * Representation of the different kind of components supported by the pulverization model.
 * The [Generic] component is used to provide a custom partitioning of the **Logical Device**.
 */
sealed interface ComponentKind {
    /**
     * **Behavior** component representing the business logic of the **Logical Device**.
     */
    data object Behavior : ComponentKind

    /**
     * **State** component representing the state of the **Logical Device**.
     */
    data object State : ComponentKind

    /**
     * **Communication** component representing the communication medium of the **Logical Device** to communicate with the other **Logical Devices**.
     */
    data object Communication : ComponentKind

    /**
     * **Sensor** component representing a sensor of the **Logical Device**.
     */
    data object Sensor : ComponentKind

    /**
     * **Actuator** component representing an actuator of the **Logical Device**.
     */
    data object Actuator : ComponentKind

    /**
     * A generic component supporting a different partitioning than the pulverization model.
     * This Kind **Must** be used only if the pulverization model is not adopted.
     */
    data object Generic : ComponentKind
}

/**
 * Symbolic reference to a [Component].
 * This concept is used to reify the link between components and used in [Component.setupWiring] for configuring the components' links.
 */
interface ComponentRef {
    /**
     * Contains the name of the specific component referenced.
     * This name is used to identify the component in the wiring.
     */
    val name: String

    /**
     * Contains the [ComponentKind] of the specific component referenced.
     */
    val type: ComponentKind

    companion object {
        /**
         * Creates a [ComponentRef] from a [component], with optionally the [type] of the component (by default set to [ComponentKind.Generic]).
         */
        fun create(
            component: Component<*>,
            type: ComponentKind = ComponentKind.Generic,
        ): ComponentRef = ComponentRefImpl(component::class.simpleName!!, type)

        /**
         * Creates a [ComponentRef] from a [C] component, with optionally the [type] of the component (by default set to [ComponentKind.Generic]).
         */
        inline fun <reified C : Component<*>> create(type: ComponentKind = ComponentKind.Generic): ComponentRef =
            ComponentRefImpl(C::class.simpleName!!, type)
    }
}

@PublishedApi
internal data class ComponentRefImpl(override val name: String, override val type: ComponentKind) : ComponentRef
