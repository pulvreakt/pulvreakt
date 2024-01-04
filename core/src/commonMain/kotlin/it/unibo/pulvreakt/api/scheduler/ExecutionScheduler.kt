package it.unibo.pulvreakt.api.scheduler

import it.unibo.pulvreakt.api.component.Component

/**
 * Represents a scheduler that can be used to trigger the execution of a [Component].
 * The scheduler is responsible for providing the next delta time at which the component should be executed.
 * The delta time is relative to the previous execution and can be retrieved by calling [timesSequence].
 * The [timesSequence] returns a sequence of delta times expressed in seconds.
 * The scheduler can be either finite or infinite, depending on the scheduler policy.
 *
 * As follows, some examples of schedulers implementations are shown.
 *
 * ```kotlin
 * class ConstantTimeScheduler(private val time: Long) : ExecutionScheduler {
 *    override fun timesSequence(): Sequence<Long> = generateSequence { time }
 * }
 * ```
 *
 * The code above shows an example of a scheduler that always returns the same time.
 * The scheduler, as shown in the example, never terminates.
 *
 * ```kotlin
 * class FiniteTimeScheduler(private val times: List<Long>) : ExecutionScheduler {
 *   override fun timesSequence(): Sequence<Long> = times.asSequence()
 * }
 * ```
 *
 * The code above shows an example of a scheduler that returns a finite sequence of times starting from the given list.
 * The scheduler, as shown in the example, terminates when no more elements in the list are available.
 *
 * ```kotlin
 * class RandomFiniteTimeScheduler(private val min: Long, private val max: Long, private val count: Int) : ExecutionScheduler {
 *  override fun timesSequence(): Sequence<Long> = generateSequence { (min..max).random() }.take(count)
 * }
 * ```
 *
 * The code above shows an example of a scheduler that returns a finite sequence of random times.
 * The scheduler, as shown in the example, terminates when the number of times generated is equal to the given count.
 *
 * ```kotlin
 * class IncrementalTimeScheduler(private val initial: Long, private val increment: Long) : ExecutionScheduler {
 *  override fun timesSequence(): Sequence<Long> = generateSequence(initial) { it + increment }
 * }
 * ```
 *
 * The code above shows an example of a scheduler that returns an infinite sequence of times starting from an initial value
 * and incrementing the value by the given increment.
 */
interface ExecutionScheduler {
    /**
     * Returns a [Sequence] containing the next time instant at which the component should be executed.
     * The sequence can be either finite or infinite.
     */
    fun timesSequence(): Sequence<Long>
}
