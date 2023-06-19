package it.unibo.pulvreakt.dsl.system

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptySet
import arrow.core.nonEmptySetOf
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate
import arrow.core.toNonEmptyListOrNull
import it.unibo.pulvreakt.core.component.ComponentOps
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.EmptyDeviceConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.UnspecifiedCapabilities
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.dsl.model.ComponentName
import it.unibo.pulvreakt.dsl.model.DeviceStructure
import it.unibo.pulvreakt.dsl.model.RequiredCapabilities

/**
 * Configure the definition of the logical device with a [deviceName].
 */
class ExtendedDeviceScope(private val deviceName: String) {
    private val componentsGraph = mutableMapOf<ComponentName, Set<ComponentName>>()
    private val requiredCapabilities = mutableMapOf<ComponentName, Set<Capability>>()

    inline fun <reified C : ComponentOps<*>> component(): ComponentName = C::class.simpleName!!

    infix fun String.wiredTo(others: NonEmptySet<ComponentName>) {
        componentsGraph[this] = others
    }

    infix fun String.wiredTo(other: ComponentName) {
        componentsGraph[this] = nonEmptySetOf(other)
    }

    infix fun ComponentName.requires(capability: Capability) {
        requiredCapabilities[this] = nonEmptySetOf(capability)
    }

    infix fun ComponentName.requires(capabilities: NonEmptySet<Capability>) {
        requiredCapabilities[this] = capabilities
    }

    private fun Raise<Nel<UnspecifiedCapabilities>>.validate(): RequiredCapabilities {
        val components = componentsGraph.keys
        val missing = requiredCapabilities.keys - components
        missing.map { UnspecifiedCapabilities(it) }.toNonEmptyListOrNull()?.let { raise(it) }
        return requiredCapabilities
    }

    internal fun generate(): Either<Nel<SystemConfigurationError>, DeviceStructure> = either {
        zipOrAccumulate(
            { ensure(componentsGraph.isNotEmpty()) { EmptyDeviceConfiguration } },
            { validate() },
        ) { _, capabilities -> DeviceStructure(deviceName, componentsGraph, capabilities) }
    }
}
