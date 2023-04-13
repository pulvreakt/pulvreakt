package it.nicolasfarabegoli.pulverization.runtime.componentsref

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.runtime.communication.Binding
import it.nicolasfarabegoli.pulverization.runtime.communication.CommManager
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import it.nicolasfarabegoli.pulverization.runtime.context.ExecutionContext
import it.nicolasfarabegoli.pulverization.runtime.dsl.StatePayload
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.time.Duration.Companion.seconds

class TestCommunicator : Communicator {
    override val remotePlaceProvider: RemotePlaceProvider
        get() = TODO("Not yet implemented")

    override suspend fun setup(binding: Binding, remotePlace: RemotePlace?) { }
    override fun receiveMessage(): Flow<ByteArray> = emptyFlow()
    override suspend fun fireMessage(message: ByteArray) { }
    override suspend fun finalize() { }
}

class ComponentRefTest : FreeSpec(), KoinTest {
    private val module = module {
        single { CommManager() }
        single<Communicator> { TestCommunicator() }
        factory<RemotePlaceProvider> {
            return@factory object : RemotePlaceProvider {
                override val context: ExecutionContext by inject()
                override fun get(type: ComponentType): RemotePlace? = null
            }
        }
    }

    init {
        "When creating a ComponentRef" - {
            "with a concrete implementation" - {
                "should rely on the communicator for communicating with another component".config(timeout = 1.seconds) {
                    PulverizationKoinModule.koinApp = koinApplication { modules(module) }
                    val stateJob = launch {
                        val behaviourRef = ComponentRefImpl<StatePayload>(State to Behaviour)
                        behaviourRef.setup()
                        behaviourRef.receiveFromComponent().first() shouldBe StatePayload(1)
                    }
                    val behaviourJob = launch {
                        val stateRef = ComponentRefImpl<StatePayload>(Behaviour to State)
                        stateRef.setup()
                        stateRef.sendToComponent(StatePayload(1))
                    }
                    behaviourJob.join()
                    stateJob.join()
                }
            }
        }
    }
}
