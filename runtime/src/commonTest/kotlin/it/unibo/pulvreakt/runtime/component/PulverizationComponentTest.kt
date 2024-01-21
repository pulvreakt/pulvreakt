package it.unibo.pulvreakt.runtime.component

import arrow.core.Either
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.api.communication.Channel
import it.unibo.pulvreakt.api.communication.protocol.Protocol
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.api.infrastructure.Host
import it.unibo.pulvreakt.api.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.runtime.communication.ChannelImpl
import it.unibo.pulvreakt.api.communication.LocalChannelManager
import it.unibo.pulvreakt.runtime.RuntimeContext
import it.unibo.pulvreakt.runtime.component.fixture.TestActuators
import it.unibo.pulvreakt.runtime.component.fixture.TestBehavior
import it.unibo.pulvreakt.runtime.component.fixture.TestCommunication
import it.unibo.pulvreakt.runtime.component.fixture.TestComponentModeReconfigurator
import it.unibo.pulvreakt.runtime.component.fixture.TestSensors
import it.unibo.pulvreakt.runtime.component.fixture.TestState
import it.unibo.pulvreakt.runtime.utils.TestProtocol
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
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class PulverizationComponentTest {
    private val cap by Capability
    private val runtimeContext = object : RuntimeContext<Int> {
        override val context: Context<Int> = object : Context<Int> {
            override val deviceId: Int = 1
            override val executingHost: Host = Host("foo", cap)

            override fun getChannel(): Channel = ChannelImpl()

            override val channelManager: LocalChannelManager = LocalChannelManager()

            override fun protocolInstance(): Protocol = TestProtocol()

            override fun <T : Any> get(key: String): T? = null

            override fun <T : Any> set(key: String, value: T) { }
        }
        override val localChannelManager: LocalChannelManager = LocalChannelManager()
        override val componentManager: ComponentManager = SimpleComponentManager()
    }

    @Test
    fun integrateFivePulverizedComponentTest() = runTest(timeout = 10.seconds) {
        val behaviour = TestBehavior().apply { setupContext(runtimeContext.context) }
        val state = TestState().apply { setupContext(runtimeContext.context) }
        val sensors = TestSensors().apply { setupContext(runtimeContext.context) }
        val actuators = TestActuators().apply { setupContext(runtimeContext.context) }
        val comm = TestCommunication().apply { setupContext(runtimeContext.context) }

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

    @Test
    fun partiallyConnectedDeviceTest() = runTest(timeout = 10.seconds) {
        val behaviour = TestBehavior().apply { setupContext(runtimeContext.context) }
        val sensors = TestSensors().apply { setupContext(runtimeContext.context) }

        behaviour.setupWiring(sensors.getRef())
        sensors.setupWiring(behaviour.getRef())

        behaviour.initialize() shouldBe Either.Right(Unit)
        sensors.initialize() shouldBe Either.Right(Unit)

        val sensJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            sensors.execute() shouldBe Either.Right(Unit)
        }

        behaviour.execute() shouldBe Either.Right(Unit)
        sensJob.cancelAndJoin()
    }
}
