package it.unibo.pulvreakt.dsl.system

import arrow.core.NonEmptySet
import arrow.core.nonEmptySetOf
import it.unibo.pulvreakt.core.component.ComponentOps
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.dsl.model.ComponentName

typealias WiringEntry = Pair<ComponentName, NonEmptySet<ComponentName>>

/**
 * Configure the definition of the logical device with a [deviceName].
 */
class ExtendedDeviceScope(private val deviceName: String) {
    inline fun <reified C : ComponentOps<*>> component(): ComponentName = C::class.simpleName!!

    infix fun String.wiredTo(other: ComponentName): WiringEntry = this to nonEmptySetOf(other)

    infix fun WiringEntry.and(other: ComponentName): WiringEntry = this.first to (this.second + other)

    infix fun ComponentName.requires(capability: String): Nothing = TODO()

    infix fun ComponentName.requires(capabilities: NonEmptySet<String>): Nothing = TODO()

    infix fun Capability.and(other: Capability): NonEmptySet<Capability> = TODO()

    internal fun generate(): Nothing = TODO()
}
