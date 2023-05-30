package it.unibo.pulvreakt.core.component

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.right
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import it.unibo.pulvreakt.core.communicator.AbstractCommunicator
import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.communicator.LocalCommunicatorManager
import it.unibo.pulvreakt.core.communicator.Mode
import it.unibo.pulvreakt.core.component.AbstractComponent.Companion.receive
import it.unibo.pulvreakt.core.component.AbstractComponent.Companion.send
import it.unibo.pulvreakt.core.reconfiguration.component.ComponentModeReconfigurator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import org.kodein.di.singleton

object DummyComponent : ComponentType

class MyComponent : AbstractComponent<Int>() {
    override val name: String = this::class.simpleName!!
    override val type: ComponentType = DummyComponent
    override suspend fun execute(): Either<String, Unit> = either {
        send<Int, MyComponent>(10).bind()
        send<Int, MyComponent>(20).bind()
    }
    companion object
}

class FakeCommunicator : AbstractCommunicator() {
    override suspend fun sendRemoteToComponent(message: ByteArray): Either<String, Unit> = Unit.right()
    override suspend fun receiveRemoteFromComponent(): Either<String, Flow<ByteArray>> = emptyFlow<ByteArray>().right()
    override suspend fun initialize(): Either<String, Unit> = Unit.right()
    override suspend fun finalize(): Either<String, Unit> = Unit.right()
}

internal class FakeComponentModeReconfigurator : ComponentModeReconfigurator {
    override fun receiveModeUpdates(): Flow<Pair<Component<*>, Mode>> = emptyFlow()
}

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
            val myComponent = MyComponent()
            val result = either {
                myComponent.setupComponentLink(myComponent)
                myComponent.initialize().bind()
                myComponent.send("Hello").bind()
            }
            when (result) {
                is Either.Left -> result.value shouldContain "setupInjector"
                is Either.Right -> error("An error must be raised when no DI is initialised")
            }
        }
        "A component should raise an error if not properly initialized" {
            val myComponent = MyComponent()
            myComponent.setupInjector(diModule)
            val sendResult = myComponent.send<Int, MyComponent>(10).leftOrNull()
                ?: error("The usage of the `send` method requires the initialization of the component")
            sendResult shouldContain "The send method must be called after the initialize one"

            val receiveResult = myComponent.receive<Int, MyComponent>().leftOrNull()
                ?: error("The usage of the `receive` method requires the initialization of the component")
            receiveResult shouldContain "The receive method must be called after the initialize one"
        }
        "A Component should send/receive messages to/from other linked components" {
            val myComponent = MyComponent().apply { setupInjector(diModule) }
            val otherComponent = MyComponent().apply { setupInjector(diModule) }

            myComponent.setupComponentLink(otherComponent)
            otherComponent.setupComponentLink(myComponent)
            myComponent.initialize() shouldBe Either.Right(Unit)
            otherComponent.initialize() shouldBe Either.Right(Unit)

            val receivedMessage = mutableListOf<Int>()

            val receiveJob = launch(UnconfinedTestDispatcher()) {
                val result = either {
                    val receiveFlow = otherComponent.receive<Int, MyComponent>().bind()
                    receiveFlow.take(1).collect {
                        receivedMessage.add(it)
                    }
                }
                result shouldBe Either.Right(Unit)
            }

            myComponent.send<Int, MyComponent>(10) shouldBe Either.Right(Unit)
            receiveJob.join()
            receivedMessage shouldBe listOf(10)
            myComponent.finalize() shouldBe Either.Right(Unit)
            otherComponent.finalize() shouldBe Either.Right(Unit)
        }
    },
)
