package it.unibo.pulvreakt.api.capabilities

class CapabilitiesMatcher<C>(private val condition: (Set<C>) -> Boolean) {
    operator fun invoke(capabilities: Set<C>): Boolean = condition(capabilities)

    companion object {
        fun <C> noRequirements(): CapabilitiesMatcher<C> = CapabilitiesMatcher { _ -> true }
    }
}
