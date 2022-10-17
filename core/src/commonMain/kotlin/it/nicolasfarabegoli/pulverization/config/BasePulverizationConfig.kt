package it.nicolasfarabegoli.pulverization.config

/**
 * Annotation class used for scoping correctly the DSL.
 */
@DslMarker
annotation class PulverizationMarker

/**
 * All the components that a [LogicalDevice] can have.
 */
enum class ComponentsType {
    STATE, ACTUATORS, SENSORS, BEHAVIOUR, COMMUNICATION
}

/**
 * Represents a _logical device_ in the pulverization context.
 * Each device is represented by its own [id] and its [components].
 */
data class LogicalDevice<I>(val id: I, val components: Set<ComponentsType> = emptySet())

typealias Topology<I> = Map<LogicalDevice<I>, Set<LogicalDevice<I>>>

/**
 * Base configuration of a pulverized system.
 * In its base version the config hold only the [topology] of the devices' network.
 */
data class PulverizationConfig<I>(val topology: Topology<I>)

/**
 * A scope to configure the topology.
 */
interface TopologyScope<I> {
    /**
     * Represent the current configured topology.
     */
    var topology: Topology<I>
}

/**
 * Configured by the [PulverizationScope] to create the topology of the network.
 * This configuration enable the creation of a custom [Topology].
 */
@PulverizationMarker
class CustomTopologyScope<I> : TopologyScope<I> {

    override var topology: Topology<I> = emptyMap()

    private fun createLink(d1: LogicalDevice<I>, d2: LogicalDevice<I>) {
        topology[d1]?.let { topology = topology + (d1 to it + d2) } ?: run { topology = topology + (d1 to setOf(d2)) }
    }

    /**
     * Method used to create a link between a [device] and its [neighbours].
     */
    fun createLinks(device: LogicalDevice<I>, neighbours: Set<LogicalDevice<I>>) {
        neighbours.forEach { createLink(device, it) }
        neighbours.forEach { createLink(it, device) }
    }
}

/**
 * This scope is responsible for give the abstraction for configuring a topology in the pulverization context.
 */
@PulverizationMarker
open class PulverizationScope<I> : TopologyScope<I> {

    override var topology: Topology<I> = emptyMap()

    /**
     * Configures a custom topology.
     */
    fun topology(init: CustomTopologyScope<I>.() -> Unit) {
        val ts = CustomTopologyScope<I>().apply(init)
        topology = ts.topology
    }

    /**
     * Creates a fully connected topology from the given [devices].
     */
    fun fullyConnectedTopology(devices: Set<LogicalDevice<I>>) {
        devices.forEach {
            topology = topology + (it to devices.filterNot { e -> e == it }.toSet())
        }
    }

    /**
     * Creates a fully connected topology from the given [devices].
     */
    fun fullyConnectedTopology(vararg devices: LogicalDevice<I>): Unit = fullyConnectedTopology(devices.toSet())
}

/**
 * Function for configure the pulverization platform and create a [PulverizationConfig].
 */
fun <I> pulverizationConfiguration(init: PulverizationScope<I>.() -> Unit): PulverizationConfig<I> {
    val config = PulverizationScope<I>().apply(init)
    return PulverizationConfig(config.topology)
}
