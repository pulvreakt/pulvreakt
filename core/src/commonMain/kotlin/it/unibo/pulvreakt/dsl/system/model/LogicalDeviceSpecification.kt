package it.unibo.pulvreakt.dsl.system.model

import it.unibo.pulvreakt.core.component.Component
import kotlin.reflect.KClass

data class LogicalDeviceSpecification(
    val deviceName: String,
    val componentsRequiredCapabilities: Map<KClass<Component<*>>, Set<Capability>>,
)
