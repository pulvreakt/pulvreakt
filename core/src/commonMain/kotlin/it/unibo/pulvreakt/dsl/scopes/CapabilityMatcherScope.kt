package it.unibo.pulvreakt.dsl.scopes

import it.unibo.pulvreakt.api.capabilities.CapabilitiesMatcher

class CapabilitiesMatcherScope<C> {
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
