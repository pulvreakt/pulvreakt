package it.nicolasfarabegoli.pulverization.runtime.componentsref

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.nicolasfarabegoli.pulverization.dsl.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.model.State
import it.nicolasfarabegoli.pulverization.runtime.communication.Binding
import it.nicolasfarabegoli.pulverization.runtime.communication.CommManager
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import it.nicolasfarabegoli.pulverization.runtime.context.ExecutionContext
import it.nicolasfarabegoli.pulverization.runtime.dsl.StatePayload
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.time.Duration.Companion.seconds

@Suppress("EmptyFunctionBlock")
class TestRemoteCommunicator(
    private val flow: MutableSharedFlow<ByteArray>,
) : Communicator {
    override suspend fun setup(binding: Binding, remotePlace: RemotePlace?) { }
    override fun receiveMessage(): Flow<ByteArray> = flow
    override suspend fun fireMessage(message: ByteArray) { flow.emit(message) }
    override suspend fun finalize() { }
}

val flow = MutableSharedFlow<ByteArray>(1)

class ComponentRefTest : FreeSpec(), KoinTest {
    private val module = module {
        single { CommManager() }
        factory<Communicator> { TestRemoteCommunicator(flow) }
        factory<RemotePlaceProvider> {
            return@factory object : RemotePlaceProvider {
                override val context: ExecutionContext by inject()
                override fun get(type: ComponentType): RemotePlace? = null
            }
        }
    }

    init {
        "When creating a ComponentRef" - {
            PulverizationKoinModule.koinApp = koinApplication { modules(module) }
            "with a concrete implementation" - {
                "should rely on the communicator for communicating with another component".config(timeout = 1.seconds) {
                    val stateJob = launch {
                        val behaviourRef = ComponentRefImpl<StatePayload>(State to Behaviour)
                        behaviourRef.setup()
                        behaviourRef.operationMode = ComponentRef.OperationMode.Local
                        behaviourRef.receiveFromComponent().first() shouldBe StatePayload(1)
                    }
                    val behaviourJob = launch {
                        val stateRef = ComponentRefImpl<StatePayload>(Behaviour to State)
                        stateRef.operationMode = ComponentRef.OperationMode.Local
                        stateRef.setup()
                        stateRef.sendToComponent(StatePayload(1))
                    }
                    behaviourJob.join()
                    stateJob.join()
                }
            }
            "can switch operation mode transparently".config(timeout = 1.seconds) {
                val behaviourRef = BehaviourRef.create<StatePayload>(State)
                val stateRef = StateRef.create<StatePayload>()
                behaviourRef.setup()
                stateRef.setup()
                stateRef.sendToComponent(StatePayload(1))
                behaviourRef.receiveFromComponent().first() shouldBe StatePayload(1)

                behaviourRef.operationMode = ComponentRef.OperationMode.Remote
                stateRef.operationMode = ComponentRef.OperationMode.Remote

                stateRef.sendToComponent(StatePayload(2))
                behaviourRef.receiveFromComponent().first() shouldBe StatePayload(2)

                behaviourRef.operationMode = ComponentRef.OperationMode.Local
                stateRef.operationMode = ComponentRef.OperationMode.Local

                stateRef.sendToComponent(StatePayload(3))
                behaviourRef.receiveFromComponent().first() shouldBe StatePayload(3)
            }
        }
    }
}
