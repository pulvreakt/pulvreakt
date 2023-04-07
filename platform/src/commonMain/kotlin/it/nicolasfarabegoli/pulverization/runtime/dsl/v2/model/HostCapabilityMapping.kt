package it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Capability

/**
 * Physical host with an [hostname], a set of [capabilities] on which pulverized components can run on.
 */
interface Host {
    /**
     * The name of the host.
     */
    val hostname: String

    /**
     * The capabilities of the host.
     */
    val capabilities: Set<Capability>
}

/**
 * Association between a [Capability] and all the [Host]s that implement it.
 */
typealias HostCapabilityMapping = Map<Capability, Set<Host>>

/**
 * Converts a set of [Host] into a [HostCapabilityMapping].
 */
fun Set<Host>.toHostCapabilityMapping(): HostCapabilityMapping {
    val capabilities = map { it.capabilities }.flatten().toSet()
    return capabilities.associateWith { key -> filter { it.capabilities.contains(key) }.toSet() }
}
