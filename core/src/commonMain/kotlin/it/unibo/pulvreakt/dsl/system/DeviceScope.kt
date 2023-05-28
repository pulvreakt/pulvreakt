package it.unibo.pulvreakt.dsl.system

import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.dsl.system.model.Capability
import it.unibo.pulvreakt.dsl.system.model.LogicalDeviceSpecification
import kotlin.reflect.KClass

class DeviceScope(private val deviceName: String) {
    private val allocationMap: MutableMap<KClass<Component<*>>, Set<Capability>> = mutableMapOf()

    inline fun <reified C : Component<*>> component(): KClass<C> = C::class

    infix fun KClass<Component<*>>.and(other: KClass<Component<*>>): Set<KClass<Component<*>>> = setOf(this, other)

    infix fun Set<KClass<Component<*>>>.and(other: KClass<Component<*>>): Set<KClass<Component<*>>> = this + other

    infix fun KClass<Component<*>>.deployableOn(capability: Capability) {
        allocationMap[this] = setOf(capability)
    }

    infix fun KClass<Component<*>>.deployableOn(capabilities: Set<Capability>) {
        allocationMap[this] = capabilities
    }

    infix fun Set<KClass<Component<*>>>.deployableOn(capability: Capability) {
        allocationMap.putAll(this.associateWith { setOf(capability) })
    }

    infix fun Set<KClass<Component<*>>>.deployableOn(capabilities: Set<Capability>) {
        allocationMap.putAll(this.associateWith { capabilities })
    }

    internal fun generate(): LogicalDeviceSpecification = LogicalDeviceSpecification(deviceName, allocationMap)
}
