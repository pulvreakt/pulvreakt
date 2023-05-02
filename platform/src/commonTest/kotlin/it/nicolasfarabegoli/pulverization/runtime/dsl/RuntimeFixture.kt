package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.dsl.model.Capability
import it.nicolasfarabegoli.pulverization.dsl.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.model.show
import it.nicolasfarabegoli.pulverization.runtime.communication.Binding
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import it.nicolasfarabegoli.pulverization.runtime.context.ExecutionContext
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.Host
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.ReconfigurationEvent
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.NewConfiguration
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.Reconfigurator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
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

object HighCpuUsage : ReconfigurationEvent<Double>() {
    override val events: Flow<Double> = emptyFlow()
    override val predicate: (Double) -> Boolean = { it > 0.75 }
}

object DeviceNetworkChange : ReconfigurationEvent<Int>() {
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
class TestCommunicator(
    private val inFlow: MutableSharedFlow<ByteArray> = MutableSharedFlow(),
    private val outFlow: MutableSharedFlow<ByteArray> = MutableSharedFlow(),
) : Communicator {
    override suspend fun setup(binding: Binding, remotePlace: RemotePlace?) {}
    override suspend fun finalize() {}
    override suspend fun fireMessage(message: ByteArray) = outFlow.emit(message)
    override fun receiveMessage(): Flow<ByteArray> = inFlow
}

@Suppress("EmptyFunctionBlock")
class TestReconfigurator(
    private val inFlow: MutableSharedFlow<NewConfiguration>,
    private val outFlow: MutableSharedFlow<NewConfiguration>,
) : Reconfigurator {
    override suspend fun reconfigure(newConfiguration: NewConfiguration) = outFlow.emit(newConfiguration)
    override fun receiveReconfiguration(): Flow<NewConfiguration> = inFlow
}

object RemotePlaceProviderTest : RemotePlaceProvider, KoinComponent {
    override val context: ExecutionContext = ExecutionContext.create("1", Host2)
    override fun get(type: ComponentType): RemotePlace = RemotePlace(type.show(), context.deviceID)
}
