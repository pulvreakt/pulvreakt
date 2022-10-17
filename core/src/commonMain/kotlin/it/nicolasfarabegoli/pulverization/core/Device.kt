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

object DeviceIDOps {
    data class StringID(val id: String) : DeviceID

    fun String.toID(): StringID = StringID(this)

    data class IntID(val id: Int) : DeviceID

    fun Int.toID(): IntID = IntID(this)
}
