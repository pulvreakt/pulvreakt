package it.nicolasfarabegoli.pulverization.runtime.componentsref

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.runtime.communication.CommManager
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import it.nicolasfarabegoli.pulverization.runtime.dsl.StatePayload
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.KoinTest

class ComponentRefTest : FreeSpec(), KoinTest {
    private val module = module {
        single { CommManager() }
        factory<RemotePlaceProvider> {
            return@factory object : RemotePlaceProvider {
                override val context: Context by inject()
                override fun get(type: ComponentType): RemotePlace? = null
            }
        }
    }

    init {
        "When creating a ComponentRef" - {
            "with a concrete implementation" - {
                "should rely on the communicator for communicating with another component" {
                    PulverizationKoinModule.koinApp = koinApplication { modules(module) }
                    val stateJob = launch {
                        val behaviourRef =
                            ComponentRefImpl<StatePayload>(State to Behaviour)
                        behaviourRef.setup()
                        behaviourRef.receiveFromComponent().first() shouldBe StatePayload(1)
                    }
                    val behaviourJob = launch {
                        val stateRef =
                            ComponentRefImpl<StatePayload>(Behaviour to State)
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
