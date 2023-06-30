package it.unibo.pulvreakt.core.component

interface ComponentRef<out T : Any> {
    val name: String

    companion object {
        fun <T : Any> create(component: Component<T>): ComponentRef<T> = ComponentRefImpl(component::class.simpleName!!)
    }

    private data class ComponentRefImpl<T : Any>(override val name: String) : ComponentRef<T>
}
