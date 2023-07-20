package it.unibo.pulvreakt.core.component

import arrow.core.Either
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.communicator.CommunicatorImpl
import it.unibo.pulvreakt.core.communicator.LocalCommunicatorManager
import it.unibo.pulvreakt.core.component.fixture.TestActuators
import it.unibo.pulvreakt.core.component.fixture.TestBehaviour
import it.unibo.pulvreakt.core.component.fixture.TestCommunication
import it.unibo.pulvreakt.core.component.fixture.TestComponentModeReconfigurator
import it.unibo.pulvreakt.core.component.fixture.TestSensors
import it.unibo.pulvreakt.core.component.fixture.TestState
import it.unibo.pulvreakt.core.context.Context
import it.unibo.pulvreakt.core.protocol.Protocol
import it.unibo.pulvreakt.core.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.core.utils.TestProtocol
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import org.kodein.di.singleton
import kotlin.test.Test

class PulverizationComponentTest {
    private val diModule = DI {
        bind<Communicator> { provider { CommunicatorImpl() } }
        bind<ComponentModeReconfigurator> { singleton { TestComponentModeReconfigurator() } }
        bind<LocalCommunicatorManager> { singleton { LocalCommunicatorManager() } }
        bind<Protocol> { singleton { TestProtocol() } }
        bind<Context> {
            singleton {
                object : Context {
                    override val deviceId: Int = 1
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun integrateFivePulverizedComponentTest() = runTest {
        val behaviour = TestBehaviour().apply { setupInjector(diModule) }
        val state = TestState().apply { setupInjector(diModule) }
        val sensors = TestSensors().apply { setupInjector(diModule) }
        val actuators = TestActuators().apply { setupInjector(diModule) }
        val comm = TestCommunication().apply { setupInjector(diModule) }

        behaviour.setupWiring(state.getRef(), sensors.getRef(), actuators.getRef(), comm.getRef())
        state.setupWiring(behaviour.getRef())
        sensors.setupWiring(behaviour.getRef())
        actuators.setupWiring(behaviour.getRef())
        comm.setupWiring(behaviour.getRef())

        behaviour.initialize() shouldBe Either.Right(Unit)
        state.initialize() shouldBe Either.Right(Unit)
        sensors.initialize() shouldBe Either.Right(Unit)
        actuators.initialize() shouldBe Either.Right(Unit)
        comm.initialize() shouldBe Either.Right(Unit)

        val sensJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            sensors.execute() shouldBe Either.Right(Unit)
        }
        val stateJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            state.execute() shouldBe Either.Right(Unit)
        }
        val actJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            actuators.execute() shouldBe Either.Right(Unit)
        }
        val commJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            comm.execute() shouldBe Either.Right(Unit)
        }

        behaviour.execute() shouldBe Either.Right(Unit)
        stateJob.cancelAndJoin()
        actJob.cancelAndJoin()
        commJob.cancelAndJoin()
        sensJob.cancelAndJoin()
    }
}
