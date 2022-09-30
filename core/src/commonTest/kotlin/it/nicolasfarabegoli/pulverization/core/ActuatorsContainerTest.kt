package it.nicolasfarabegoli.pulverization.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer.Companion.getActuator
import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer.Companion.getActuators
import it.nicolasfarabegoli.pulverization.core.ActuatorsFixtures.MyActuator1
import it.nicolasfarabegoli.pulverization.core.ActuatorsFixtures.MyActuator2
import it.nicolasfarabegoli.pulverization.core.ActuatorsFixtures.DummyActuator

class ActuatorsContainerTest : FunSpec({
    context("ActuatorsContainer tests") {
        test("The container when queried with an `Actuator` type should give the right instance") {
            val myActuator1 = MyActuator1(10)
            val container = ActuatorsContainer<Int>().apply {
                addActuator(myActuator1)
                addActuator(MyActuator2(11))
                addActuator(MyActuator2(12))
            }

            container.getActuators(MyActuator1::class).size shouldBe 1
            container.getActuators<Int, ActuatorPayload, MyActuator1>().size shouldBe 1
            container.getActuators(MyActuator2::class).size shouldBe 2
            container.getActuators(DummyActuator::class).size shouldBe 0
            container.getActuator(MyActuator1::class)?.let { it shouldBeSameInstanceAs myActuator1 }
            container.getActuator<Int, ActuatorPayload, MyActuator1>()?.let { it shouldBeSameInstanceAs myActuator1 }
            container.getActuator(MyActuator2::class) shouldNotBe null
            container.getActuator(DummyActuator::class) shouldBe null
            container.getActuator<Int, ActuatorPayload, DummyActuator>() shouldBe null
        }

        test("An empty container should return empty set or null") {
            val container = ActuatorsContainer<Int>()
            container.getActuator(DummyActuator::class) shouldBe null
            container.getActuators(MyActuator1::class).size shouldBe 0
        }
    }
})
