package it.unibo.pulvreakt.core.component.fixture

import it.unibo.pulvreakt.api.scheduler.TimeDistribution

class SimpleTimeDistribution : TimeDistribution {
    override fun nextTimeInstant(): Long = 1000
    override fun isCompleted(): Boolean = false
}

class FiniteShotTimeDistribution(
    private val count: Int,
    private val time: Long = 1000,
) : TimeDistribution {
    private var currentCount = 0
    override fun nextTimeInstant(): Long {
        currentCount++
        return time
    }

    override fun isCompleted(): Boolean = currentCount == count
}
