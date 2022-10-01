package it.nicolasfarabegoli.pulverization.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import it.nicolasfarabegoli.pulverization.core.ActuatorsFixtures.DummyActuator
import it.nicolasfarabegoli.pulverization.core.ActuatorsFixtures.MyActuator1
import it.nicolasfarabegoli.pulverization.core.ActuatorsFixtures.MyActuator2

class ActuatorsContainerTest : FunSpec(
    {
        context("ActuatorsContainer tests") {
            test("The container when queried with an `Actuator` type should give the right instance") {
                val myActuator1 = MyActuator1(10)
                val container = ActuatorsContainer<Int>().apply {
                    this += myActuator1
                    this += (MyActuator2(11))
                    this += (MyActuator2(12))
                }

                container.getAll(MyActuator1::class).size shouldBe 1
                container.getActuators<Int, Int, MyActuator1>().size shouldBe 1
                container.getAll(MyActuator2::class).size shouldBe 2
                container.getAll(DummyActuator::class).size shouldBe 0
                container[MyActuator1::class]?.let { it shouldBeSameInstanceAs myActuator1 }
                container.getActuator<Int, Int, MyActuator1>()?.let { it shouldBeSameInstanceAs myActuator1 }
                container[MyActuator2::class] shouldNotBe null
                container[DummyActuator::class] shouldBe null
                container.getActuator<Int, Int, DummyActuator>() shouldBe null
            }

            test("An empty container should return empty set or null") {
                val container = ActuatorsContainer<Int>()
                container[DummyActuator::class] shouldBe null
                container.getAll(MyActuator1::class).size shouldBe 0
            }
        }
    },
)
