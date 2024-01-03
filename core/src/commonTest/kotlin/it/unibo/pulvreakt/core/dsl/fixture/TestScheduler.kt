package it.unibo.pulvreakt.core.dsl.fixture

import it.unibo.pulvreakt.api.scheduler.TimeDistribution

class TestScheduler : TimeDistribution {
    override fun nextTimeInstant(): Long = 1
    override fun isCompleted(): Boolean = false
}
