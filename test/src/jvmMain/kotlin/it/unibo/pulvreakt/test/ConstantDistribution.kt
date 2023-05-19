package it.unibo.pulvreakt.test

import it.unibo.pulvreakt.core.component.time.TimeDistribution

class ConstantDistribution(private val time: Long) : TimeDistribution {
    override fun nextTimeInstant(): Long = time
    override fun isCompleted(): Boolean = false
}
