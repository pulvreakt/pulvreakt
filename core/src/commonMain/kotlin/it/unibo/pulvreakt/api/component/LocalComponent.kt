package it.unibo.pulvreakt.api.component

sealed interface LocalComponent<out Output, Capability> {
    val componentName: String

    companion object {
        fun <O, Ca, C : Component<O, *>> C.toLocalComponent(): LocalComponent<O, Ca> =
            LocalComponentImpl(this::class.qualifiedName ?: error("Cannot build local component from $this"))

        fun <O, C> named(name: String): LocalComponent<O, C> = LocalComponentImpl(name)
    }
}

private class LocalComponentImpl<Output, Capability>(override val componentName: String) : LocalComponent<Output, Capability>
