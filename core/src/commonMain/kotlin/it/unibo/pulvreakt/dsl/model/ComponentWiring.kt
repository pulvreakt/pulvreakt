package it.unibo.pulvreakt.dsl.model

import arrow.core.NonEmptySet
import it.unibo.pulvreakt.core.component.Component
import kotlin.reflect.KClass

sealed interface ComponentType {
    companion object {
        fun ctypeOf(component: KClass<out Component<*>>): ComponentType = ComponentTypeImpl(component)
        inline fun <reified C : Component<*>> ctypeOf(): ComponentType = ctypeOf(C::class)
        fun Component<*>.getType(): ComponentType = ComponentTypeImpl(this::class)
        private data class ComponentTypeImpl(val componentType: KClass<out Component<*>>) : ComponentType
    }
}
typealias ComponentsGraph = Map<ComponentType, Set<ComponentType>>
typealias RequiredCapabilities = Map<ComponentType, NonEmptySet<Capability>>

data class DeviceStructure(
    val deviceName: String,
    val componentsGraph: ComponentsGraph,
    val requiredCapabilities: RequiredCapabilities,
)

typealias ConfiguredDeviceStructure = NonEmptySet<DeviceStructure>

operator fun ConfiguredDeviceStructure.get(deviceName: String): DeviceStructure? = find { it.deviceName == deviceName }
