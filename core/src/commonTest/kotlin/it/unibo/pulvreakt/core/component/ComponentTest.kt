package it.unibo.pulvreakt.core.component

import arrow.core.Either
import arrow.core.raise.either
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.communicator.CommunicatorImpl
import it.unibo.pulvreakt.core.communicator.LocalCommunicatorManager
import it.unibo.pulvreakt.core.component.AbstractComponent.Companion.receive
import it.unibo.pulvreakt.core.component.AbstractComponent.Companion.send
import it.unibo.pulvreakt.core.component.errors.ComponentError
import it.unibo.pulvreakt.core.component.fixture.TestActuators
import it.unibo.pulvreakt.core.component.fixture.TestBehaviour
import it.unibo.pulvreakt.core.component.fixture.TestCommunication
import it.unibo.pulvreakt.core.component.fixture.TestComponentModeReconfigurator
import it.unibo.pulvreakt.core.component.fixture.TestSensors
import it.unibo.pulvreakt.core.component.fixture.TestSensorsComponent
import it.unibo.pulvreakt.core.component.fixture.TestState
import it.unibo.pulvreakt.core.context.Context
import it.unibo.pulvreakt.core.protocol.Protocol
import it.unibo.pulvreakt.core.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.core.utils.TestProtocol
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import org.kodein.di.singleton

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalStdlibApi::class)
class ComponentTest : StringSpec(
    {
        coroutineTestScope = true
        val diModule = DI {
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

        "A Component should raise an error if the DI module is not initialized" {
            val component = TestSensorsComponent()

            val result = either {
                component.setupWiring(component.getRef())
                component.initialize().bind()
                component.send(component.getRef(), 10).bind()
            }

            when (result) {
                is Either.Left -> result.value shouldBe ComponentError.InjectorNotInitialized
                is Either.Right -> error("An error must be raised when no DI is initialised")
            }
        }
        "A component should raise an error if not properly initialized" {
            val component = TestSensorsComponent().apply { setupInjector(diModule) }

            val sendResult = component.send(component.getRef(), 10).leftOrNull()
                ?: error("The usage of the `send` method requires the initialization of the component")
            sendResult shouldBe ComponentError.ComponentNotInitialized

            val receiveResult = component.receive<Int>(component.getRef()).leftOrNull()
                ?: error("The usage of the `receive` method requires the initialization of the component")
            receiveResult shouldBe ComponentError.ComponentNotInitialized
        }
        "A Component should send/receive messages to/from other linked components" {
            val myComponent = TestSensorsComponent().apply { setupInjector(diModule) }
            val otherComponent = TestSensorsComponent().apply { setupInjector(diModule) }

            myComponent.setupWiring(otherComponent.getRef())
            otherComponent.setupWiring(myComponent.getRef())
            myComponent.initialize() shouldBe Either.Right(Unit)
            otherComponent.initialize() shouldBe Either.Right(Unit)

            val receivedMessage = mutableListOf<Int>()

            val receiveJob = launch(UnconfinedTestDispatcher(testCoroutineScheduler)) {
                val result = either {
                    val receiveFlow = otherComponent.receive<Int>(myComponent.getRef()).bind()
                    receiveFlow.take(1).collect {
                        receivedMessage.add(it)
                    }
                }
                result shouldBe Either.Right(Unit)
            }

            myComponent.send(otherComponent.getRef(), 10) shouldBe Either.Right(Unit)
            receiveJob.join()
            receivedMessage shouldBe listOf(10)
            myComponent.finalize() shouldBe Either.Right(Unit)
            otherComponent.finalize() shouldBe Either.Right(Unit)
        }
        "The Behaviour component should have linked all the other components, otherwise it should raise an error" {
            val behaviour = TestBehaviour().apply { setupInjector(diModule) }
            val state = TestState().apply { setupInjector(diModule) }
            behaviour.setupWiring(state.getRef())
            state.setupWiring(behaviour.getRef())

            behaviour.initialize() shouldBe Either.Right(Unit)
            state.initialize() shouldBe Either.Right(Unit)

            when (val error = behaviour.execute().leftOrNull() ?: error("An error must be raised")) {
                is ComponentError.ExecutionError -> error.message shouldContain "No component of type"
                else -> error("The error raised must be an `ExecutionError`")
            }
        }
        "The Behaviour should execute its logic without error when properly configured" {
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

            val sensJob = launch(UnconfinedTestDispatcher(testCoroutineScheduler)) {
                sensors.execute() shouldBe Either.Right(Unit)
            }
            val stateJob = launch(UnconfinedTestDispatcher(testCoroutineScheduler)) {
                state.execute() shouldBe Either.Right(Unit)
            }
            val actJob = launch(UnconfinedTestDispatcher(testCoroutineScheduler)) {
                actuators.execute() shouldBe Either.Right(Unit)
            }
            val commJob = launch(UnconfinedTestDispatcher(testCoroutineScheduler)) {
                comm.execute() shouldBe Either.Right(Unit)
            }

            behaviour.execute() shouldBe Either.Right(Unit)
            stateJob.cancelAndJoin()
            actJob.cancelAndJoin()
            commJob.cancelAndJoin()
            sensJob.cancelAndJoin()
        }
    },
)
