package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Capability
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ReconfigurationEvent
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.toHostCapabilityMapping
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

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

object Host3 : Host {
    override val hostname: String = "host3"
    override val capabilities: Set<Capability> = setOf(HighCpu, HighMemory)
}

object CpuUsage : ReconfigurationEvent<Double> {
    override val events: Flow<Double> = emptyFlow()
    override val predicate: (Double) -> Boolean = { it > 0.75 }
}

object DeviceNetworkChange : ReconfigurationEvent<Int> {
    override val events: Flow<Int> = emptyFlow()
    override val predicate: (Int) -> Boolean = { it > 10 }
}

val capabilityMapping = setOf(Host1, Host2).toHostCapabilityMapping()

val memoryUsageFlow = flow {
    while (true) {
        val deviceMemoryUsage = Random.nextDouble()
        emit(deviceMemoryUsage)
        delay(1.seconds)
    }
}
