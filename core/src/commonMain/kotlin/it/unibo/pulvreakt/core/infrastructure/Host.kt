package it.unibo.pulvreakt.core.infrastructure

import arrow.core.NonEmptySet
import arrow.core.nonEmptySetOf
import it.unibo.pulvreakt.dsl.system.model.Capability
import kotlin.reflect.KProperty

/**
 * Represents the host of the available infrastructure.
 */
sealed interface Host {
    val hostname: String
    val capabilities: NonEmptySet<Capability>

    companion object {
        operator fun invoke(hostname: String, capabilities: NonEmptySet<Capability>): Host = HostImpl(hostname, capabilities)
    }

    private data class HostImpl(override val hostname: String, override val capabilities: NonEmptySet<Capability>) : Host {
        override fun toString(): String = hostname
    }
}

class HostDelegate(private val capabilities: NonEmptySet<Capability>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Host = Host(property.name, capabilities)

    companion object {
        operator fun invoke(firstCapability: Capability, vararg capabilities: Capability): HostDelegate =
            HostDelegate(nonEmptySetOf(firstCapability, *capabilities))
        operator fun invoke(capabilities: NonEmptySet<Capability>): HostDelegate =
            HostDelegate(capabilities)
    }
}
