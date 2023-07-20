package it.unibo.pulvreakt.runtime.unit.component

import arrow.core.Either
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.runtime.unit.component.errors.ComponentNotRegistered

class SimpleComponentManagerTest : StringSpec({
    coroutineTestScope = true
    "The ComponentManager should return an empty set when no components are registered in the manager" {
        val componentManager = SimpleComponentManager()
        componentManager.alive() shouldBe emptySet()
    }
    "The ComponentManager should return an empty set when no component is started" {
        val componentManager = SimpleComponentManager()
        componentManager.register(TestComponent1())
        componentManager.alive() shouldBe emptySet()
    }
    "The ComponentManager should start successfully a registered component" {
        val componentManager = SimpleComponentManager()
        val component = TestComponent1()
        componentManager.register(component)
        componentManager.start(component.getRef()).isRight() shouldBe true
        componentManager.alive() shouldBe setOf(component.getRef())
    }
    "The ComponentManager should stop successfully a registered and started component" {
        val componentManager = SimpleComponentManager()
        val component = TestComponent1()
        componentManager.register(component)
        componentManager.alive() shouldBe emptySet()
        componentManager.start(component.getRef()).isRight() shouldBe true
        componentManager.alive() shouldBe setOf(component.getRef())
        componentManager.stop(component.getRef()) shouldBe Either.Right(Unit)
        componentManager.alive() shouldBe emptySet()
    }
    "The ComponentManager should raise and error when try to start a non-registered component" {
        val componentManager = SimpleComponentManager()
        val component = TestComponent1()
        val result = componentManager.start(component.getRef()).leftOrNull()
            ?: error("An error must be raised when try to start a non-registered component")
        result shouldBe ComponentNotRegistered(component.getRef())
    }
    "The ComponentManager should raise and error when try to stop a non-registered component" {
        val componentManager = SimpleComponentManager()
        val component = TestComponent1()
        val result = componentManager.stop(component.getRef()).leftOrNull()
            ?: error("An error must be raised when try to stop a non-registered component")
        result shouldBe ComponentNotRegistered(component.getRef())
        componentManager.alive() shouldBe emptySet()
    }
    "The ComponentManager should not raise an error if a component is cancelled twice" {
        val componentManager = SimpleComponentManager()
        val component = TestComponent2()
        componentManager.register(component)
        componentManager.start(component.getRef()).isRight() shouldBe true
        componentManager.stop(component.getRef()) shouldBe Either.Right(Unit)
        componentManager.stop(component.getRef()) shouldBe Either.Right(Unit)
    }
})
