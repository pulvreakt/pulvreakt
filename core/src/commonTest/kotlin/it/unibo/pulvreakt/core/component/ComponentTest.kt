package it.unibo.pulvreakt.core.component

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.right
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.communicator.Mode
import it.unibo.pulvreakt.core.component.AbstractComponent.Companion.send
import it.unibo.pulvreakt.core.reconfiguration.component.ComponentModeReconfigurator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import org.kodein.di.singleton

class MyComponent : AbstractComponent<Int>() {
    override val name: String = this::class.simpleName!!
    override suspend fun execute(): Either<String, Unit> = either {
        send<Int, MyComponent>(10).bind()
        send<Int, MyComponent>(20).bind()
    }
}

class FakeCommunicator : Communicator {
    override lateinit var di: DI
    override suspend fun communicatorSetup(source: Component<*>, destination: Component<*>) = Unit.right()
    override suspend fun sendToComponent(message: ByteArray): Either<String, Unit> = Unit.right()
    override fun setupInjector(kodein: DI) { di = kodein }
    override suspend fun receiveFromComponent(): Either<String, Flow<ByteArray>> = emptyFlow<ByteArray>().right()
    override suspend fun initialize(): Either<String, Unit> = Unit.right()
    override suspend fun finalize(): Either<String, Unit> = Unit.right()
    override fun setMode(mode: Mode) = Unit
}

internal class FakeComponentModeReconfigurator : ComponentModeReconfigurator {
    override fun receiveModeUpdates(): Flow<Pair<Component<*>, Mode>> = emptyFlow()
}

class ComponentTest : StringSpec({
    val diModule = DI {
        bind<Communicator> { provider { FakeCommunicator() } }
        bind<ComponentModeReconfigurator> { singleton { FakeComponentModeReconfigurator() } }
    }

    "A Component should raise an error if the DI module is not initialized" {
        val myComponent = MyComponent()
        val result = either {
            myComponent.setupComponentLink(myComponent)
            myComponent.initialize()
            myComponent.send("Hello").bind()
        }
        when (result) {
            is Either.Left -> result.value shouldContain "setupInjector"
            is Either.Right -> error("An error must be raised when no DI is initialised")
        }
    }
    "A Component should send messages to other linked components" {
        val myComponent = MyComponent().apply {
            setupInjector(diModule)
            setupComponentLink(this)
            initialize() shouldBe Either.Right(Unit)
        }
        myComponent.execute() shouldBe Either.Right(Unit)
        myComponent.finalize()
    }
})
