package it.unibo.pulvreakt.dsl.scopes

import arrow.core.Either
import it.unibo.pulvreakt.api.capabilities.CapabilitiesMatcher
import it.unibo.pulvreakt.api.component.Component
import it.unibo.pulvreakt.api.component.LocalComponent
import kotlin.properties.ReadOnlyProperty

data class ComponentBuilder<Output, Capability>(val componentLogic: () -> Either<Any, Output>)

class PulverizationScope {
    private val components = mutableMapOf<LocalComponent<*, *>, Component<*, *>>()

    fun <Output, Capability> component(block: () -> Either<Any, Output>): ComponentBuilder<Output, Capability> = ComponentBuilder(block)

    fun <Output, Capability, C : Component<Output, Capability>> component(component: C): ReadOnlyProperty<Any?, LocalComponent<Output, Capability>> =
        ReadOnlyProperty { _, property ->
            LocalComponent.named<Output, Capability>(property.name).also { localComponent ->
                components[localComponent] = component
            }
        }

    infix fun <Output, Capability> ComponentBuilder<Output, Capability>.requires(
        block: CapabilitiesMatcherScope<Capability>.() -> Unit,
    ): ReadOnlyProperty<Any?, LocalComponent<Output, Capability>> = ReadOnlyProperty { _, property ->
        val matcher = CapabilitiesMatcherScope<Capability>().apply { block() }.generate()
        val component = object : Component<Output, Capability> {
            override val requiresToBeExecuted: CapabilitiesMatcher<Capability> = matcher
            override suspend fun execute(): Either<Any, Output> = componentLogic()
        }
        LocalComponent.named<Output, Capability>(property.name).also { localComponent ->
            components[localComponent] = component
        }
    }

    fun <Output, Capability> ComponentBuilder<Output, Capability>.noRequirements(): ReadOnlyProperty<Any?, LocalComponent<Output, Capability>> =
        ReadOnlyProperty { _, property ->
            val component = object : Component<Output, Capability> {
                override val requiresToBeExecuted: CapabilitiesMatcher<Capability> = CapabilitiesMatcher.noRequirements()
                override suspend fun execute(): Either<Any, Output> = componentLogic()
            }
            LocalComponent.named<Output, Capability>(property.name).also { localComponent ->
                components[localComponent] = component
            }
        }
}
