package it.nicolasfarabegoli.pulverization.core

/**
 * All the components that a [LogicalDevice] can have.
 */
enum class ComponentsType {
    STATE, ACTUATORS, SENSORS, BEHAVIOUR, COMMUNICATION
}

/**
 * Marker interface representing the *ID* of the [LogicalDevice].
 */
interface DeviceID

/**
 * Represents a _logical device_ in the pulverization context.
 * Each device is represented by its own [id] and its [components].
 */
data class LogicalDevice<I : DeviceID>(val id: I, val components: Set<ComponentsType> = emptySet())

/**
 * Enrichment for [DeviceID].
 */
object DeviceIDOps {
    /**
     * A [DeviceID] which models the [id] with a [String].
     */
    data class StringID(val id: String) : DeviceID

    /**
     * Utility method for converting a [String] into a [StringID].
     */
    fun String.toID(): StringID = StringID(this)

    /**
     * A [DeviceID] which models the [id] with an [Int].
     */
    data class IntID(val id: Int) : DeviceID

    /**
     * Utility method for converting an [Int] into an [IntID].
     */
    fun Int.toID(): IntID = IntID(this)
}
