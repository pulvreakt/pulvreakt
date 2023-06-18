package it.unibo.pulvreakt.dsl.model

import kotlin.reflect.KProperty

/**
 * Express a capacity that a Component requires to be executed.
 */
sealed interface Capability

class CapabilityDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Capability = CapabilityImpl(property.name)

    private data class CapabilityImpl(val name: String) : Capability {
        override fun toString(): String = name
    }
}
