package it.nicolasfarabegoli.pulverization.runtime.utils

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Capability
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.v2.pulverizationSystem
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.pulverizationRuntime

object HighCpu : Capability
object EmbeddedDevice : Capability

object Host1 : Host {
    override val capabilities: Set<Capability> = setOf(HighCpu)
    override val hostname: String = "host1"
}

object Host2 : Host {
    override val capabilities: Set<Capability> = setOf(EmbeddedDevice)
    override val hostname: String = "host2"
}

val systemConfig = pulverizationSystem {
    device("smartphone") {
        Behaviour deployableOn setOf(HighCpu, EmbeddedDevice)
        Sensors deployableOn EmbeddedDevice
    }
}

val availableHosts = setOf(Host1, Host2)


