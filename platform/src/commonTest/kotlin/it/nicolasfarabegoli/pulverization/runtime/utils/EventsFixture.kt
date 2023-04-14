package it.nicolasfarabegoli.pulverization.runtime.utils

import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ReconfigurationEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

val highCpuUsageFlow = MutableSharedFlow<Double>(1)

object HighCpuUsage : ReconfigurationEvent<Double> {
    override val events: Flow<Double> = highCpuUsageFlow
    override val predicate: (Double) -> Boolean = { it > 0.75 }
}
