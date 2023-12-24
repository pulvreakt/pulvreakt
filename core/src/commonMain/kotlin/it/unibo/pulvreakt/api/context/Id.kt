package it.unibo.pulvreakt.api.context

import kotlinx.serialization.Serializable

/**
 * Represents the id of a device.
 */
@Serializable
sealed interface Id

/**
 * Implementation of [Id] using an [Int] as [value] for the id.
 * This implementation inherits the [Comparable] interface to allow the comparison between two [IntId].
 */
@Serializable
class IntId(val value: Int) : Id, Comparable<IntId> {
    override fun compareTo(other: IntId): Int {
        return value.compareTo(other.value)
    }

    companion object {
        /**
         * Creates a new [IntId] from an [Int].
         */
        fun Int.toId(): IntId = IntId(this)

        /**
         * Converts an [IntId] to an [Int].
         */
        fun IntId.toInt(): Int = value
    }
}
