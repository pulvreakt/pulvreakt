package it.unibo.pulvreakt.runtime.unit.component

import arrow.core.Either
import arrow.core.right
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.ComponentType

object DummyComponent : ComponentType
class C1 : AbstractComponent<Int>() {
    override val name: String = "C1"
    override val type: ComponentType = DummyComponent
    override suspend fun execute(): Either<String, Unit> = Unit.right()
}

class SimpleComponentManagerTest : StringSpec({
    coroutineTestScope = true
    "The ComponentManager should return an empty set when no components are registered in the manager" {
        val componentManager = SimpleComponentManager()
        componentManager.alive() shouldBe emptySet()
    }
    "The ComponentManager should return an empty set when no component is started" {
        val componentManager = SimpleComponentManager()
        componentManager.register(C1())
        componentManager.alive() shouldBe emptySet()
    }
    "The ComponentManager should start successfully a registered component" {
        val componentManager = SimpleComponentManager()
        val component = C1()
        componentManager.register(component)
        componentManager.start(component) shouldBe Either.Right(Unit)
        componentManager.alive() shouldBe setOf(component)
    }
    "The ComponentManager should stop successfully a registered and started component" {
        val componentManager = SimpleComponentManager()
        val component = C1()
        componentManager.register(component)
        componentManager.alive() shouldBe emptySet()
        componentManager.start(component) shouldBe Either.Right(Unit)
        componentManager.alive() shouldBe setOf(component)
        componentManager.stop(component) shouldBe Either.Right(Unit)
        componentManager.alive() shouldBe emptySet()
    }
    "The ComponentManager should raise and error when try to start a non-registered component" {
        val componentManager = SimpleComponentManager()
        val component = C1()
        val result = componentManager.start(component).leftOrNull()
            ?: error("An error must be raised when try to start a non-registered component")
        result shouldContain "Unable to execute it"
    }
    "The ComponentManager should raise and error when try to stop a non-registered component" {
        val componentManager = SimpleComponentManager()
        val component = C1()
        val result = componentManager.stop(component).leftOrNull()
            ?: error("An error must be raised when try to stop a non-registered component")
        result shouldContain "Unable to stop it"
        componentManager.alive() shouldBe emptySet()
    }
    "The ComponentManager should not raise an error if a component is cancelled twice" {
        val componentManager = SimpleComponentManager()
        val component = C1()
        componentManager.register(component)
        componentManager.start(component) shouldBe Either.Right(Unit)
        componentManager.stop(component) shouldBe Either.Right(Unit)
        componentManager.stop(component) shouldBe Either.Right(Unit)
    }
})
