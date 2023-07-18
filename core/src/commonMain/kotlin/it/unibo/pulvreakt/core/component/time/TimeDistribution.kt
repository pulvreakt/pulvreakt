package it.unibo.pulvreakt.core.component.time

import it.unibo.pulvreakt.core.component.Component

/**
 * Represents a time distribution that can be used to trigger the execution of a [Component].
 * The time distribution can be queried to get the next time instant at which the component should be executed.
 * The time distribution can also be queried to know if it has been completed.
 */
interface TimeDistribution {
    /**
     * Returns the next time instant at which the component should be executed.
     */
    fun nextTimeInstant(): Long

    /**
     * Returns true if the time distribution has been completed, false otherwise.
     */
    fun isCompleted(): Boolean
}
