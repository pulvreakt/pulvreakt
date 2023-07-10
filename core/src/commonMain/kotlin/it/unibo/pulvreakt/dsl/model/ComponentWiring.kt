package it.unibo.pulvreakt.dsl.model

import arrow.core.NonEmptySet
import it.unibo.pulvreakt.core.component.ComponentRef

// /**
// * Represents the type of a component.
// */
// sealed interface ComponentType {
//    companion object {
//        /**
//         * Creates a component type from the [component].
//         */
//        fun ctypeOf(component: KClass<out Component>): ComponentType = ComponentTypeImpl(component)
//
//        /**
//         * Creates a component type from the class [C] of the component.
//         */
//        inline fun <reified C : Component> ctypeOf(): ComponentType = ctypeOf(C::class)
//
//        /**
//         * Extension function to get the [ComponentType] from a component.
//         */
//        fun Component.getType(): ComponentType = ComponentTypeImpl(this::class)
//        private data class ComponentTypeImpl(val componentType: KClass<out Component>) : ComponentType
//    }
// }
typealias ComponentsGraph = Map<ComponentRef, Set<ComponentRef>>
typealias RequiredCapabilities = Map<ComponentRef, NonEmptySet<Capability>>

/**
 * Represents the structure of a device characterized by a [deviceName],
 * a [componentsGraph] that describes the links between the components of the device,
 * and the [requiredCapabilities] of each component.
 */
data class DeviceStructure(
    val deviceName: String,
    val componentsGraph: ComponentsGraph,
    val requiredCapabilities: RequiredCapabilities,
)

typealias ConfiguredDeviceStructure = NonEmptySet<DeviceStructure>

/**
 * Extension function to get a [DeviceStructure] from a [ConfiguredDeviceStructure] by its [deviceName].
 */
operator fun ConfiguredDeviceStructure.get(deviceName: String): DeviceStructure? = find { it.deviceName == deviceName }
