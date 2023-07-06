package it.unibo.pulvreakt.core.component

/**
 * Symbolic reference to a component.
 */
interface ComponentRef<out T : Any> {
    /**
     * The name of the symbolic reference.
     */
    val name: String

    companion object {
        /**
         * Creates a [ComponentRef] from a [component].
         */
        fun <T : Any> create(component: Component<T>): ComponentRef<T> =
            ComponentRefImpl(component::class.simpleName!!)

        /**
         * Creates a [ComponentRef] from a [Component] type.
         */
        inline fun <reified C : Component<T>, T : Any> create(): ComponentRef<T> =
            ComponentRefImpl(C::class.simpleName!!)
    }
}

@PublishedApi
internal data class ComponentRefImpl<T : Any>(override val name: String) : ComponentRef<T>
