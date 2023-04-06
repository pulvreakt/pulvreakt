package it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Capability

/**
 * Physical host with an [hostname], a set of [capabilities] on which pulverized components can run on.
 */
interface Host {
    val hostname: String
    val capabilities: Set<Capability>
}

typealias HostCapabilityMapping = Map<Capability, Set<Host>>

fun Set<Host>.toHostCapabilityMapping(): HostCapabilityMapping {
    val capabilities = map { it.capabilities }.flatten().toSet()
    return capabilities.associateWith { key -> filter { it.capabilities.contains(key) }.toSet() }
}
