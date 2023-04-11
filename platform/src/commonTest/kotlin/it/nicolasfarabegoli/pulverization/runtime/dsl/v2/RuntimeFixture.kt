package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Capability
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ReconfigurationEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

object HighCpu : Capability
object HighMemory : Capability
object EmbeddedDevice : Capability

object Host1 : Host {
    override val hostname: String = "host1"
    override val capabilities: Set<Capability> = setOf(HighCpu, HighMemory)
}

object Host2 : Host {
    override val hostname: String = "host2"
    override val capabilities: Set<Capability> = setOf(EmbeddedDevice)
}

object CpuUsage : ReconfigurationEvent<Double> {
    override val events: Flow<Double> = emptyFlow()
    override val predicate: (Double) -> Boolean = { it > 0.75 }
}

object DeviceNetworkChange : ReconfigurationEvent<Int> {
    override val events: Flow<Int> = emptyFlow()
    override val predicate: (Int) -> Boolean = { it > 10 }
}
