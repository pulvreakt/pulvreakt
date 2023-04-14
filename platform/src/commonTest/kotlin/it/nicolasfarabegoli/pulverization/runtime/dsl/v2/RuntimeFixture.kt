package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Capability
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.dsl.v2.model.show
import it.nicolasfarabegoli.pulverization.runtime.communication.Binding
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import it.nicolasfarabegoli.pulverization.runtime.context.ExecutionContext
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ReconfigurationEvent
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.toHostCapabilityMapping
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.Reconfigurator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
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

object HighCpuUsage : ReconfigurationEvent<Double> {
    override val events: Flow<Double> = emptyFlow()
    override val predicate: (Double) -> Boolean = { it > 0.75 }
}

object DeviceNetworkChange : ReconfigurationEvent<Int> {
    override val events: Flow<Int> = emptyFlow()
    override val predicate: (Int) -> Boolean = { it > 10 }
}

val memoryUsageFlow = flow {
    while (true) {
        val deviceMemoryUsage = Random.nextDouble()
        emit(deviceMemoryUsage)
        delay(1.seconds)
    }
}

@Suppress("EmptyFunctionBlock")
class TestCommunicator : Communicator {
    override val remotePlaceProvider: RemotePlaceProvider
        get() = TODO("Not yet implemented")

    override suspend fun setup(binding: Binding, remotePlace: RemotePlace?) { }

    override suspend fun finalize() { }

    override suspend fun fireMessage(message: ByteArray) { }

    override fun receiveMessage(): Flow<ByteArray> = emptyFlow()
}

@Suppress("EmptyFunctionBlock")
class TestReconfigurator(private val flow: MutableSharedFlow<Pair<ComponentType, Host>>) : Reconfigurator {
    override suspend fun reconfigure(newConfiguration: Pair<ComponentType, Host>) {
        flow.emit(newConfiguration)
    }
    override fun receiveReconfiguration(): Flow<Pair<ComponentType, Host>> = flow
}

object RPP : RemotePlaceProvider, KoinComponent {
    override val context: ExecutionContext = ExecutionContext.create("1", Host2)
    override fun get(type: ComponentType): RemotePlace = RemotePlace(type.show(), context.deviceID)
}
