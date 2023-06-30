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
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.EmptyDeviceConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.UnspecifiedCapabilities
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.dsl.model.ComponentType
import it.unibo.pulvreakt.dsl.model.DeviceStructure
import it.unibo.pulvreakt.dsl.model.RequiredCapabilities

/**
 * Configure the definition of the logical device with a [deviceName].
 */
class ExtendedDeviceScope(private val deviceName: String) {
    private val componentsGraph = mutableMapOf<ComponentType, Set<ComponentType>>()
    private val requiredCapabilities = mutableMapOf<ComponentType, Set<Capability>>()

//    inline fun <reified C : Component> component(): ComponentType {
//        val componentName = C::class.simpleName!!
//        addComponent(componentName)
//        return componentName
//    }
    inline fun <reified C : Component<*>> withComponent(): ComponentType =
        ComponentType.ctypeOf<C>().also { addComponent(it) }

    infix fun ComponentType.wiredTo(others: NonEmptySet<ComponentType>) {
        componentsGraph[this] = others
    }

    infix fun ComponentType.wiredTo(other: ComponentType) {
        componentsGraph[this] = nonEmptySetOf(other)
    }

    infix fun ComponentType.requires(capability: Capability) {
        requiredCapabilities[this] = nonEmptySetOf(capability)
    }

    infix fun ComponentType.requires(capabilities: NonEmptySet<Capability>) {
        requiredCapabilities[this] = capabilities
    }

    fun addComponent(component: ComponentType) {
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
