package it.unibo.pulvreakt.runtime.component.fixture

import it.unibo.pulvreakt.api.scheduler.ExecutionScheduler

class SimpleExecutionScheduler : ExecutionScheduler {
    override fun timesSequence(): Sequence<Long> = generateSequence { 1000L }
}

class FiniteShotExecutionScheduler(
    private val count: Int,
    private val time: Long = 1000,
) : ExecutionScheduler {
    override fun timesSequence(): Sequence<Long> = generateSequence { time }.take(count)
}
