package it.unibo.pulvreakt.core.component.fixture

import it.unibo.pulvreakt.core.component.time.TimeDistribution

class SimpleTimeDistribution : TimeDistribution {
    override fun nextTimeInstant(): Long = 1000
    override fun isCompleted(): Boolean = true
}

class OneShotTimeDistribution : TimeDistribution {
    private var completed = false
    override fun nextTimeInstant(): Long {
        completed = true
        return 1000
    }

    override fun isCompleted(): Boolean = completed
}
