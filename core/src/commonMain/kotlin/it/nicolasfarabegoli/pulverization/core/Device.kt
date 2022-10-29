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

/**
 * Get the [C] component from a [LogicalDevice], null is returned if no component is found.
 */
inline fun <reified C : PulverizedComponent> LogicalDevice.get(): C? = components.filterIsInstance<C>().firstOrNull()

/**
 * Enrichment for [DeviceID].
 */
object DeviceIDOps {
    /**
     * A [DeviceID] which models the [id] with a [String].
     */
    data class StringID(private val id: String) : DeviceID {
        override fun show(): String = id
    }

    /**
     * Utility method for converting a [String] into a [StringID].
     */
    fun String.toID(): StringID = StringID(this)

    /**
     * A [DeviceID] which models the [id] with an [Int].
     */
    data class IntID(private val id: Int) : DeviceID {
        override fun show(): String = id.toString()
    }

    /**
     * Utility method for converting an [Int] into an [IntID].
     */
    fun Int.toID(): IntID = IntID(this)
}
