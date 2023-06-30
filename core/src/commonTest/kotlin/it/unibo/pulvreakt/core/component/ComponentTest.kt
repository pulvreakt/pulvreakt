package it.unibo.pulvreakt.core.component

import io.kotest.core.spec.style.StringSpec
import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.communicator.LocalCommunicatorManager
import it.unibo.pulvreakt.core.component.fixture.FakeCommunicator
import it.unibo.pulvreakt.core.component.fixture.FakeComponentModeReconfigurator
import it.unibo.pulvreakt.core.reconfiguration.component.ComponentModeReconfigurator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import org.kodein.di.singleton

@OptIn(ExperimentalCoroutinesApi::class)
class ComponentTest : StringSpec(
    {
        coroutineTestScope = true
        val diModule = DI {
            bind<Communicator> { provider { FakeCommunicator() } }
            bind<ComponentModeReconfigurator> { singleton { FakeComponentModeReconfigurator() } }
            bind<LocalCommunicatorManager> { singleton { LocalCommunicatorManager() } }
        }

        "A Component should raise an error if the DI module is not initialized" {
            error("Not implemented yet")
//            val myComponent = MyComponent()
//            val result = either {
//                myComponent.setupComponentLink(myComponent)
//                myComponent.initialize().bind()
//                myComponent.send(DummyComponent, 15).bind()
//            }
//            when (result) {
//                is Either.Left -> result.value shouldContain "setupInjector"
//                is Either.Right -> error("An error must be raised when no DI is initialised")
//            }
        }
        "A component should raise an error if not properly initialized" {
            error("Not implemented yet")
//            val myComponent = MyComponent()
//            myComponent.setupInjector(diModule)
//            val sendResult = myComponent.send(DummyComponent, 10).leftOrNull()
//                ?: error("The usage of the `send` method requires the initialization of the component")
//            sendResult shouldContain "The send method must be called after the initialize one"
//
//            val receiveResult = myComponent.receive(DummyComponent).leftOrNull()
//                ?: error("The usage of the `receive` method requires the initialization of the component")
//            receiveResult shouldContain "The receive method must be called after the initialize one"
        }
        "A Component should send/receive messages to/from other linked components" {
            error("Not implemented yet")
//            val myComponent = MyComponent().apply { setupInjector(diModule) }
//            val otherComponent = MyComponent().apply { setupInjector(diModule) }
//
//            myComponent.setupComponentLink(otherComponent)
//            otherComponent.setupComponentLink(myComponent)
//            myComponent.initialize() shouldBe Either.Right(Unit)
//            otherComponent.initialize() shouldBe Either.Right(Unit)
//
//            val receivedMessage = mutableListOf<Int>()
//
//            val receiveJob = launch(UnconfinedTestDispatcher()) {
//                val result = either {
//                    val receiveFlow = otherComponent.receive(DummyComponent).bind()
//                    receiveFlow.take(1).collect {
//                        receivedMessage.add(it)
//                    }
//                }
//                result shouldBe Either.Right(Unit)
//            }
//
//            myComponent.send(DummyComponent, 10) shouldBe Either.Right(Unit)
//            receiveJob.join()
//            receivedMessage shouldBe listOf(10)
//            myComponent.finalize() shouldBe Either.Right(Unit)
//            otherComponent.finalize() shouldBe Either.Right(Unit)
        }
    },
)
