package it.unibo.pulvreakt.dsl.scopes

import it.unibo.pulvreakt.api.capabilities.CapabilitiesMatcher

class RequirementScope<Capability> {
    private var capabilityMatcher = CapabilitiesMatcher.noRequirements<Capability>()

    fun <C> requires(block: CapabilitiesMatcherScope<C>.() -> Unit): CapabilitiesMatcher<C> =
        CapabilitiesMatcherScope<C>().apply { block() }.generate()

    internal fun buildMatcher(): CapabilitiesMatcher<Capability> = capabilityMatcher
}
