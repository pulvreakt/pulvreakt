package it.nicolasfarabegoli.pulverization.config

import it.nicolasfarabegoli.pulverization.core.LogicalDevice
import it.nicolasfarabegoli.pulverization.core.PulverizedComponent

/**
 * Annotation class used for scoping correctly the DSL.
 */
@DslMarker
annotation class PulverizationMarker

/**
 * TODO.
 */
data class LogicalDeviceConfiguration(val components: Map<List<PulverizedComponent>, Tier>)

/**
 * Base configuration for the pulverized system.
 */
interface BasePulverizationConfig<D> {
    /**
     * All the devices in the configuration.
     */
    val devices: Map<D, LogicalDeviceConfiguration>
}

/**
 * TODO.
 */
data class BasePulverizationConfigImpl<D>(override val devices: Map<D, LogicalDeviceConfiguration>) :
    BasePulverizationConfig<D>

inline fun <D, reified C1 : PulverizedComponent> BasePulverizationConfig<D>.changeTheName(name: D, run: (C1) -> Unit) {
    devices[name]?.let {
        val componentsList = it.components.keys
            .filter { e -> e.size == 1 }
            .firstOrNull { l -> l.any { i -> i is C1 } } ?: error("No component ${C1::class} fount in $name")
        val component1 = componentsList.filterIsInstance<C1>().firstOrNull()!!
        run(component1)
    } ?: error("No device of type $name is found!")
}

inline fun <D, reified C1, reified C2> BasePulverizationConfig<D>.changeName(name: D, run: (C1, C2) -> Unit)
    where C1 : PulverizedComponent, C2 : PulverizedComponent {
    devices[name]?.let {
        val componentsList = it.components.keys
            .filter { e -> e.size == 2 }
            .filter { l -> l.any { i -> i is C1 } }
            .firstOrNull { l -> l.any { i -> i is C2 } }
            ?: error("No component ${C1::class.simpleName}, ${C2::class.simpleName} found in $name")
        val component1 = componentsList.filterIsInstance<C1>().firstOrNull()!!
        val component2 = componentsList.filterIsInstance<C2>().firstOrNull()!!
        run(component1, component2)
    } ?: error("No device of type $name is found!")
}

inline fun <D, reified C1, reified C2, reified C3> BasePulverizationConfig<D>.changeName(
    name: D,
    run: (C1, C2, C3) -> Unit,
) where C1 : PulverizedComponent, C2 : PulverizedComponent, C3 : PulverizedComponent {
    devices[name]?.let {
        val componentsList = it.components.keys
            .filter { e -> e.size == 3 }
            .filter { l -> l.any { i -> i is C1 } }
            .filter { l -> l.any { i -> i is C2 } }
            .firstOrNull { l -> l.any { i -> i is C3 } }
            ?: error(
                "No component ${C1::class.simpleName}, ${C2::class.simpleName}, ${C3::class.simpleName} found in $name",
            )
        val component1 = componentsList.filterIsInstance<C1>().firstOrNull()!!
        val component2 = componentsList.filterIsInstance<C2>().firstOrNull()!!
        val component3 = componentsList.filterIsInstance<C3>().firstOrNull()!!
        run(component1, component2, component3)
    } ?: error("No device of type $name is found!")
}

inline fun <D, reified C1, reified C2, reified C3, reified C4> BasePulverizationConfig<D>.changeName(
    name: D,
    run: (C1, C2, C3, C4) -> Unit,
) where C1 : PulverizedComponent, C2 : PulverizedComponent, C3 : PulverizedComponent, C4 : PulverizedComponent {
    devices[name]?.let {
        val componentsList = it.components.keys
            .filter { e -> e.size == 4 }
            .filter { l -> l.any { i -> i is C1 } }
            .filter { l -> l.any { i -> i is C2 } }
            .filter { l -> l.any { i -> i is C3 } }
            .firstOrNull { l -> l.any { i -> i is C4 } }
            ?: error(
                "No component ${C1::class.simpleName}, ${C2::class.simpleName}, ${C3::class.simpleName}," +
                    " ${C4::class.simpleName} found in $name",
            )
        val component1 = componentsList.filterIsInstance<C1>().firstOrNull()!!
        val component2 = componentsList.filterIsInstance<C2>().firstOrNull()!!
        val component3 = componentsList.filterIsInstance<C3>().firstOrNull()!!
        val component4 = componentsList.filterIsInstance<C4>().firstOrNull()!!
        run(component1, component2, component3, component4)
    } ?: error("No device of type $name is found!")
}

inline fun <D, reified C1, reified C2, reified C3, reified C4, reified C5> BasePulverizationConfig<D>.changeName(
    name: D,
    run: (C1, C2, C3, C4, C5) -> Unit,
) where C1 : PulverizedComponent,
        C2 : PulverizedComponent,
        C3 : PulverizedComponent,
        C4 : PulverizedComponent,
        C5 : PulverizedComponent {
    devices[name]?.let {
        val componentsList = it.components.keys
            .filter { e -> e.size == 5 }
            .filter { l -> l.any { i -> i is C1 } }
            .filter { l -> l.any { i -> i is C2 } }
            .filter { l -> l.any { i -> i is C3 } }
            .filter { l -> l.any { i -> i is C4 } }
            .firstOrNull { l -> l.any { i -> i is C5 } }
            ?: error(
                "No component ${C1::class.simpleName}, ${C2::class.simpleName}, ${C3::class.simpleName}," +
                    " ${C4::class.simpleName}, ${C5::class.simpleName} found in $name",
            )
        val component1 = componentsList.filterIsInstance<C1>().firstOrNull()!!
        val component2 = componentsList.filterIsInstance<C2>().firstOrNull()!!
        val component3 = componentsList.filterIsInstance<C3>().firstOrNull()!!
        val component4 = componentsList.filterIsInstance<C4>().firstOrNull()!!
        val component5 = componentsList.filterIsInstance<C5>().firstOrNull()!!
        run(component1, component2, component3, component4, component5)
    } ?: error("No device of type $name is found!")
}

/**
 * Scope for building DSL.
 */
interface Scope<S> {
    /**
     * Build the object configured by the DSL.
     */
    fun build(): S
}

/**
 * Scope for configuring a specific [LogicalDevice].
 */
@PulverizationMarker
class LogicalDeviceScope : Scope<LogicalDeviceConfiguration> {
    private val componentsMap: MutableMap<List<PulverizedComponent>, Tier> = mutableMapOf()

    infix fun <C : PulverizedComponent> List<C>.deployableOn(tier: Tier) {
        componentsMap[this] = tier
    }

    infix fun <C : PulverizedComponent> C.deployableOn(tier: Tier) {
        componentsMap[listOf(this)] = tier
    }

    infix fun <C : PulverizedComponent> C.and(other: C): List<C> = listOf(this, other)
    infix fun <C : PulverizedComponent> List<C>.and(other: C): List<C> = this + other

    /**
     * Creates a new [LogicalDevice].
     */
    override fun build(): LogicalDeviceConfiguration = LogicalDeviceConfiguration(componentsMap)
}

sealed interface Tier
object Cloud : Tier
object Edge : Tier
object Device : Tier

/**
 * Scope for creating [LogicalDevice]s.
 */
@PulverizationMarker
open class PulverizationConfigScope<D> : Scope<BasePulverizationConfig<D>> {
    private val logicalDevices: MutableMap<D, LogicalDeviceConfiguration> = mutableMapOf()

    /**
     * Creates a new [LogicalDevice] assigning to it a [name].
     */
    fun logicalDevice(name: D, init: LogicalDeviceScope.() -> Unit) {
        if (logicalDevices.containsKey(name)) error("$name already registered")
        val lDeviceScope = LogicalDeviceScope().apply(init)
        logicalDevices[name] = lDeviceScope.build()
    }

    /**
     * Creates a new [BasePulverizationConfig].
     */
    override fun build(): BasePulverizationConfig<D> = BasePulverizationConfigImpl(logicalDevices)
}

/**
 * Entrypoint for the DSL for building the configuration.
 */
fun <D> pulverizationConfig(init: PulverizationConfigScope<D>.() -> Unit): BasePulverizationConfig<D> =
    PulverizationConfigScope<D>().apply(init).build()

// sealed interface Devices
// object Device1 : Devices
//
// data class SS(val i: Int) : StateRepresentation
// class St : State<SS> {
//    override val context: Context by inject()
//    override fun get(): SS {
//        TODO("Not yet implemented")
//    }
//
//    override fun update(newState: SS): SS {
//        TODO("Not yet implemented")
//    }
// }
//
// fun test() {
//    val config = pulverizationConfig<Devices> {
//        logicalDevice(Device1) {
//            St() and St() deployableOn Cloud
//            St() deployableOn Device
//        }
//    }
// }
