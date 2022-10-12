package it.nicolasfarabegoli.pulverization.core

import io.kotest.assertions.failure
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import it.nicolasfarabegoli.pulverization.core.ActuatorsFixtures.MyActuator1
import it.nicolasfarabegoli.pulverization.core.ActuatorsFixtures.MyActuator2

class ActuatorsContainerTest : FunSpec(
    {
        context("ActuatorsContainer tests") {
            test("To the container can be added multiple actuators at once") {
                val container = ActuatorsContainer<Int>()
                container.addAll(MyActuator1(10), MyActuator1(11), MyActuator2(12))
                container.getAll<MyActuator1>().size shouldBe 2
                container.getAll<MyActuator2>().size shouldBe 1
            }
            test("To the container can be added a single actuator") {
                val container = ActuatorsContainer<Int>()
                container += MyActuator1(10)
                container.get<MyActuator1>() shouldNotBe null
                container.getAll<MyActuator1>().size shouldBe 1
            }
            test("The container could be queried using the KClass") {
                val container = ActuatorsContainer<Int>().apply { addAll(MyActuator1(10), MyActuator2(11)) }
                container[MyActuator1::class] shouldNotBe null
                container.getAll(MyActuator2::class).size shouldBe 1
            }
            test("The container, when queried, should return the actuator in the lambda") {
                val actuator2 = MyActuator2(3)
                val container = ActuatorsContainer<Int>().apply { addAll(MyActuator1(1), MyActuator1(2), actuator2) }
                container.get<MyActuator2> { this shouldBeSameInstanceAs actuator2 }
                container.getAll<MyActuator1> { size shouldBe 2 }
            }
            test("The container, when queried with an invalid class, should not execute the lambda") {
                ActuatorsContainer<Int>().get<MyActuator1> {
                    failure("This lambda should not be executed since no ${MyActuator1::class} is available")
                }
            }
        }
    },
)
