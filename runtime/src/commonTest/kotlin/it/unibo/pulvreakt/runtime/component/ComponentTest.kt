package it.unibo.pulvreakt.runtime.component

import arrow.core.Either
import arrow.core.raise.either
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.api.communication.Channel
import it.unibo.pulvreakt.api.communication.protocol.Protocol
import it.unibo.pulvreakt.api.component.AbstractComponent.Companion.receive
import it.unibo.pulvreakt.api.component.AbstractComponent.Companion.send
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.api.infrastructure.Host
import it.unibo.pulvreakt.api.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.errors.component.ComponentError
import it.unibo.pulvreakt.runtime.communication.ChannelImpl
import it.unibo.pulvreakt.runtime.communication.LocalChannelManager
import it.unibo.pulvreakt.runtime.component.fixture.TestComponentModeReconfigurator
import it.unibo.pulvreakt.runtime.component.fixture.TestSensorsComponent
import it.unibo.pulvreakt.runtime.utils.TestProtocol
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import org.kodein.di.singleton

@OptIn(ExperimentalCoroutinesApi::class)
class ComponentTest : StringSpec(
    {
        coroutineTestScope = true
        val cap by Capability
        val diModule = DI {
            bind<Channel> { provider { ChannelImpl() } }
            bind<ComponentModeReconfigurator> { singleton { TestComponentModeReconfigurator() } }
            bind<LocalChannelManager> { singleton { LocalChannelManager() } }
            bind<Protocol> { singleton { TestProtocol() } }
            bind<Context<Int>> { singleton { Context(1, Host("foo", cap)) } }
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

            val receiveResult = component.receive<Int, Int>(component.getRef()).leftOrNull()
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

            val receiveJob = launch(UnconfinedTestDispatcher()) {
                val result = either {
                    val receiveFlow = otherComponent.receive<Int, Int>(myComponent.getRef()).bind()
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
        // Due to an (apparently) bug in Kotest (https://github.com/kotest/kotest/issues/3575) other tests are in `PulverizationComponentTest`
    },
)
