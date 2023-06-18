package it.unibo.pulvreakt.dsl.system

/**
 * System DSL scope for configuring logical devices.
 */
class SystemSpecificationScope {
    fun extendedLogicDevice(name: String, config: ExtendedDeviceScope.() -> Unit): Nothing = TODO()

    fun logicDevice(name: String, config: CanonicalDeviceScope.() -> Unit): Nothing = TODO()

    internal fun generate(): Nothing = TODO()
}
