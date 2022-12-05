package it.nicolasfarabegoli.pulverization.core

/**
 * Marker interface representing the *ID* of the [LogicalDevice].
 */
interface DeviceID {
    /**
     * The string representation of the [DeviceID].
     * By default calls the [toString] method. It's recommended to override this behaviour with a proper representation.
     */
    fun show(): String = toString()
}

// /**
// * Represents a _logical device_ in the pulverization context.
// * Each device is represented by its own [id] and its [components].
// */
// data class LogicalDevice<I : DeviceID>(val id: I, val components: Set<ComponentsType> = emptySet())
/**
 * Abstraction of a device in a pulverized system.
 */
interface LogicalDevice {
    /**
     * All the components that constitute this specific [LogicalDevice].
     */
    val components: Set<PulverizedComponent>
}
