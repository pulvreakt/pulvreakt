package it.unibo.pulvreakt.api.infrastructure

import arrow.core.NonEmptySet
import arrow.core.nonEmptySetOf
import it.unibo.pulvreakt.dsl.model.Capability

/**
 * Models the concept of a **host** belonging to the infrastructure.
 *
 * A host in a pulverization system manage the execution of one or more components of one or more logical devices.
 * The host is identified by its [hostname] and it has a set of [Capability] that defines the capabilities of the host.
 *
 * The capabilities are used to define the constraints on where a component can be deployed.
 * If a component `A` requires the capabilities `c1` and `c2` to be deployed,
 * the target host must have both the capabilities `c1` and `c2` to deploy the component `A`.
 * In other words, a host must exhibit a super-set of the capabilities required by a component to be deployed.
 */
sealed interface Host {
    /**
     * The hostname of the host.
     * This is used to identify the host in the infrastructure and must be unique.
     */
    val hostname: String

    /**
     * The set of [Capability] of the host.
     * This set defines the capabilities of the host and is used to define the constraints on where a component can be deployed.
     * A host must exhibit at least one capability, as reported in the [NonEmptySet] type.
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
