package it.unibo.pulvreakt.api.scheduler

import it.unibo.pulvreakt.api.component.Component

/**
 * Represents a scheduler that can be used to trigger the execution of a [Component].
 * The scheduler is responsible for providing the next delta time at which the component should be executed.
 * The delta time is relative to the previous execution and can be retrieved by calling [timesSequence].
 * The [timesSequence] returns a sequence of delta times expressed in seconds.
 * The scheduler can be either finite or infinite, depending on the scheduler policy.
 */
interface ExecutionScheduler {
    /**
     * Returns a [Sequence] containing the next time instant at which the component should be executed.
     * The sequence can be either finite or infinite.
     */
    fun timesSequence(): Sequence<Long>
}
