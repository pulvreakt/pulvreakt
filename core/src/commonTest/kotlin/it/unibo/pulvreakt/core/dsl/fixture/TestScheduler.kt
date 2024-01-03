package it.unibo.pulvreakt.core.dsl.fixture

import it.unibo.pulvreakt.api.scheduler.ExecutionScheduler

class TestScheduler : ExecutionScheduler {
    override fun timesSequence(): Sequence<Long> = generateSequence { 1L }
}
