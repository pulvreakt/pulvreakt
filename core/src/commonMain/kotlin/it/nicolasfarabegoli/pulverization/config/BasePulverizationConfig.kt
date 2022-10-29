package it.nicolasfarabegoli.pulverization.config

import it.nicolasfarabegoli.pulverization.core.LogicalDevice
import it.nicolasfarabegoli.pulverization.core.PulverizedComponent

/**
 * Annotation class used for scoping correctly the DSL.
 */
@DslMarker
annotation class PulverizationMarker

/**
 * Simple representation of a logical device which holds its [components].
 */
data class SimpleLogicalDevice(override val components: Set<PulverizedComponent>) : LogicalDevice

/**
 * Base configuration for the pulverized system.
 * The configuration stores all the [devices] expressed as a map "device nam -> [LogicalDevice]".
 */
data class BasePulverizationConfig(val devices: Map<String, LogicalDevice>)

/**
 * Scope for configuring a specific [LogicalDevice].
 */
@PulverizationMarker
class LogicalDeviceScope {
    private val components: MutableSet<PulverizedComponent> = mutableSetOf()

    /**
     * Add a [PulverizedComponent] to the [LogicalDevice].
     */
    fun <C : PulverizedComponent> component(component: C) {
        components += component
    }

    /**
     * Creates a new [LogicalDevice].
     */
    fun build(): LogicalDevice = SimpleLogicalDevice(components)
}

/**
 * Scope for creating [LogicalDevice]s.
 */
@PulverizationMarker
class PulverizationConfigScope {
    private val logicalDevices: MutableMap<String, LogicalDevice> = mutableMapOf()

    /**
     * Creates a new [LogicalDevice] assigning to it a [name].
     */
    fun logicalDevice(name: String, init: LogicalDeviceScope.() -> Unit) {
        if (logicalDevices.keys.contains(name)) error("The device `$name` already exist")
        val logicalDevice = LogicalDeviceScope().apply(init).build()
        logicalDevices += name to logicalDevice
    }

    /**
     * Creates a new [BasePulverizationConfig].
     */
    fun build(): BasePulverizationConfig = BasePulverizationConfig(logicalDevices)
}

/**
 * Entrypoint for the DSL for building the configuration.
 */
fun pulverizationConfig(init: PulverizationConfigScope.() -> Unit): BasePulverizationConfig =
    PulverizationConfigScope().apply(init).build()

// typealias Topology<I> = Map<LogicalDevice<I>, Set<LogicalDevice<I>>>
//
// /**
// * Base configuration of a pulverized system.
// * In its base version the config hold only the [topology] of the devices' network.
// */
// data class PulverizationConfig<I : DeviceID>(val topology: Topology<I>)
//
// /**
// * A scope to configure the topology.
// */
// interface TopologyScope<I : DeviceID> {
//    /**
//     * Represent the current configured topology.
//     */
//    var topology: Topology<I>
// }
//
// /**
// * Configured by the [PulverizationScope] to create the topology of the network.
// * This configuration enable the creation of a custom [Topology].
// */
// @PulverizationMarker
// class CustomTopologyScope<I : DeviceID> : TopologyScope<I> {
//
//    override var topology: Topology<I> = emptyMap()
//
//    private fun createLink(d1: LogicalDevice<I>, d2: LogicalDevice<I>) {
//        topology[d1]?.let { topology = topology + (d1 to it + d2) } ?: run { topology = topology + (d1 to setOf(d2)) }
//    }
//
//    /**
//     * Method used to create a link between a [device] and its [neighbours].
//     */
//    fun createLinks(device: LogicalDevice<I>, neighbours: Set<LogicalDevice<I>>) {
//        neighbours.forEach { createLink(device, it) }
//        neighbours.forEach { createLink(it, device) }
//    }
// }
//
// /**
// * This scope is responsible for give the abstraction for configuring a topology in the pulverization context.
// */
// @PulverizationMarker
// open class PulverizationScope<I : DeviceID> : TopologyScope<I> {
//
//    override var topology: Topology<I> = emptyMap()
//
//    /**
//     * Configures a custom topology.
//     */
//    fun topology(init: CustomTopologyScope<I>.() -> Unit) {
//        val ts = CustomTopologyScope<I>().apply(init)
//        topology = ts.topology
//    }
//
//    /**
//     * Creates a fully connected topology from the given [devices].
//     */
//    fun fullyConnectedTopology(devices: Set<LogicalDevice<I>>) {
//        devices.forEach {
//            topology = topology + (it to devices.filterNot { e -> e == it }.toSet())
//        }
//    }
//
//    /**
//     * Creates a fully connected topology from the given [devices].
//     */
//    fun fullyConnectedTopology(vararg devices: LogicalDevice<I>): Unit = fullyConnectedTopology(devices.toSet())
// }
//
// /**
// * Function for configure the pulverization platform and create a [PulverizationConfig].
// */
// fun <I : DeviceID> pulverizationConfiguration(init: PulverizationScope<I>.() -> Unit): PulverizationConfig<I> {
//    val config = PulverizationScope<I>().apply(init)
//    return PulverizationConfig(config.topology)
// }
