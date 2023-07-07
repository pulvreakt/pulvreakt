package it.unibo.pulvreakt.core.infrastructure

import arrow.core.NonEmptySet
import arrow.core.nonEmptySetOf
import it.unibo.pulvreakt.dsl.model.Capability

/**
 * Represents the host of the available infrastructure.
 */
sealed interface Host {
    /**
     * The hostname of the host.
     */
    val hostname: String

    /**
     * The set of [Capability] of the host.
     */
    val capabilities: NonEmptySet<Capability>

    companion object {
        /**
         * Creates a [Host] with the given [hostname] and [capabilities] as a non-empty set.
         */
        operator fun invoke(hostname: String, capabilities: NonEmptySet<Capability>): Host =
            HostImpl(hostname, capabilities)

        /**
         * Creates a [Host] with the given [hostname] and [capability].
         */
        operator fun invoke(hostname: String, capability: Capability, vararg otherCapabilities: Capability): Host =
            HostImpl(hostname, nonEmptySetOf(capability, *otherCapabilities))
    }

    private data class HostImpl(override val hostname: String, override val capabilities: NonEmptySet<Capability>) : Host {
        override fun toString(): String = hostname
    }
}
