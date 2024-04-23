package it.unibo.pulvreakt.api.capabilities

import it.unibo.pulvreakt.dsl.model.Capability

class CapabilitiesMatcher<C : Capability>(private val condition: (Set<C>) -> Boolean) {
    operator fun invoke(capabilities: Set<C>): Boolean = condition(capabilities)

    companion object {
        fun noRequirements(): CapabilitiesMatcher<Capability> = CapabilitiesMatcher { _ -> true }
    }
}

class CapabilitiesMatcherScope<C : Capability> {
    private var predicate = CapabilitiesMatcher<C> { _ -> true }

    infix fun C.and(other: C): CapabilitiesMatcher<C> =
        CapabilitiesMatcher { capabilities -> capabilities.contains(this) && capabilities.contains(other) }.also { predicate = it }

    infix fun CapabilitiesMatcher<C>.and(other: CapabilitiesMatcher<C>): CapabilitiesMatcher<C> =
        CapabilitiesMatcher { capabilities -> this(capabilities) && other(capabilities) }.also { predicate = it }

    infix fun CapabilitiesMatcher<C>.and(other: C): CapabilitiesMatcher<C> =
        CapabilitiesMatcher { capabilities -> this(capabilities) && capabilities.contains(other) }.also { predicate = it }

    infix fun C.or(other: C): CapabilitiesMatcher<C> =
        CapabilitiesMatcher { capabilities -> capabilities.contains(this) || capabilities.contains(other) }.also { predicate = it}

    infix fun C.or(other: CapabilitiesMatcher<C>): CapabilitiesMatcher<C> =
        CapabilitiesMatcher { capabilities -> capabilities.contains(this) || other(capabilities) }.also { predicate = it }

    infix fun CapabilitiesMatcher<C>.or(other: C, ): CapabilitiesMatcher<C> =
        CapabilitiesMatcher { capabilities -> this(capabilities) || capabilities.contains(other) }.also { predicate = it }

    fun the(capability: C): CapabilitiesMatcher<C> = CapabilitiesMatcher { capabilities -> capabilities.contains(capability) }.also { predicate = it }

    internal fun generate(): CapabilitiesMatcher<C> = predicate
}

fun <C : Capability> requires(block: CapabilitiesMatcherScope<C>.() -> Unit): CapabilitiesMatcher<C> =
    CapabilitiesMatcherScope<C>().apply { block() }.generate()

val A by Capability
val B by Capability
val E by Capability

val predicate = CapabilitiesMatcher.noRequirements()

fun main() {
    val capabilities = setOf(B, E)
    println(predicate(capabilities))
}
