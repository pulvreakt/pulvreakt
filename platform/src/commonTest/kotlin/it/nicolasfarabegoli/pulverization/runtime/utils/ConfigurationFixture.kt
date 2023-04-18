package it.nicolasfarabegoli.pulverization.runtime.utils

import it.nicolasfarabegoli.pulverization.dsl.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.model.Capability
import it.nicolasfarabegoli.pulverization.dsl.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.pulverizationSystem
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.Host
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.NewConfiguration
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.Reconfigurator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

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

class TestReconfigurator(
    private val inFlow: MutableSharedFlow<NewConfiguration>,
    private val outFlow: MutableSharedFlow<NewConfiguration>,
) : Reconfigurator {
    override suspend fun reconfigure(newConfiguration: NewConfiguration) {
        outFlow.emit(newConfiguration)
    }

    override fun receiveReconfiguration(): Flow<NewConfiguration> = inFlow
}
