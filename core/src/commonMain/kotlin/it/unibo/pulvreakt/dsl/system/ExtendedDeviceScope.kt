package it.unibo.pulvreakt.dsl.system

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptySet
import arrow.core.nonEmptyListOf
import arrow.core.nonEmptySetOf
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.toNonEmptyListOrNull
import arrow.core.toNonEmptySetOrNull
import it.unibo.pulvreakt.api.component.Component
import it.unibo.pulvreakt.api.component.ComponentKind
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.dsl.errors.EmptyDeviceConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError
import it.unibo.pulvreakt.dsl.errors.UnspecifiedCapabilities
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.dsl.model.DeviceStructure
import it.unibo.pulvreakt.dsl.model.RequiredCapabilities

/**
 * Configure the definition of the logical device with a [deviceName].
 */
class ExtendedDeviceScope(private val deviceName: String) {
    private val componentsGraph = mutableMapOf<ComponentRef, Set<ComponentRef>>()
    private val requiredCapabilities = mutableMapOf<ComponentRef, Set<Capability>>()

    /**
     * Register a [Component] in the device and return its [ComponentKind].
     */
    inline fun <reified C : Component> withComponent(): ComponentRef =
        ComponentRef.create<C>(ComponentKind.Generic).also { addComponent(it) }

    /**
     * Links a component to other components.
     */
    infix fun ComponentRef.wiredTo(others: NonEmptySet<ComponentRef>) {
        componentsGraph[this] = others
    }

    /**
     * Links a component to another component.
     */
    infix fun ComponentRef.wiredTo(other: ComponentRef) {
        componentsGraph[this] = nonEmptySetOf(other)
    }

    /**
     * Specifies that a component requires a capability.
     */
    infix fun ComponentRef.requires(capability: Capability) {
        requiredCapabilities[this] = nonEmptySetOf(capability)
    }

    /**
     * Specifies that a component requires a set of capabilities.
     */
    infix fun ComponentRef.requires(capabilities: NonEmptySet<Capability>) {
        requiredCapabilities[this] = capabilities
    }

    /**
     * Register a component in the device.
     */
    fun addComponent(component: ComponentRef) {
        componentsGraph[component] ?: run { componentsGraph[component] = emptySet() }
    }

    private fun Raise<Nel<UnspecifiedCapabilities>>.validateCapabilities(): RequiredCapabilities {
        val components = componentsGraph.keys
        ensure(requiredCapabilities.isNotEmpty()) { components.map { UnspecifiedCapabilities(it) }.toNonEmptyListOrNull()!! }
        val missing = requiredCapabilities.keys - components
        missing.map { UnspecifiedCapabilities(it) }.toNonEmptyListOrNull()?.let { raise(it) }
        return requiredCapabilities.mapValues { (_, value) -> value.toNonEmptySetOrNull()!! }
    }

    internal fun generate(): Either<Nel<SystemConfigurationError>, DeviceStructure> = either {
        ensure(componentsGraph.isNotEmpty()) { nonEmptyListOf(EmptyDeviceConfiguration) }
        val capabilities = validateCapabilities()
        DeviceStructure(deviceName, componentsGraph, capabilities)
    }
}
