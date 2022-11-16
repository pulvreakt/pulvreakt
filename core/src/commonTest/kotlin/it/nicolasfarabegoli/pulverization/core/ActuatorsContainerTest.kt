package it.nicolasfarabegoli.pulverization.core

import io.kotest.assertions.failure
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.ActuatorsFixtures.MyActuator1
import it.nicolasfarabegoli.pulverization.core.ActuatorsFixtures.MyActuator2
import it.nicolasfarabegoli.pulverization.dsl.FuzzyContext

class ActuatorsContainerTest : FunSpec(
    {
        context("ActuatorsContainer tests") {
            test("To the container can be added multiple actuators at once") {
                val container = object : ActuatorsContainer() {
                    override val context: Context = FuzzyContext
                }
                container.addAll(MyActuator1(), MyActuator1(), MyActuator2())
                container.getAll<MyActuator1>().size shouldBe 2
                container.getAll<MyActuator2>().size shouldBe 1
            }
            test("To the container can be added a single actuator") {
                val container = object : ActuatorsContainer() {
                    override val context: Context = FuzzyContext
                }
                container += MyActuator1()
                container.get<MyActuator1>() shouldNotBe null
                container.getAll<MyActuator1>().size shouldBe 1
            }
            test("The container could be queried using the KClass") {
                val container = object : ActuatorsContainer() {
                    override val context: Context = FuzzyContext
                }.apply { addAll(MyActuator1(), MyActuator2()) }
                container[MyActuator1::class] shouldNotBe null
                container.getAll(MyActuator2::class).size shouldBe 1
            }
            test("The container, when queried, should return the actuator in the lambda") {
                val actuator2 = MyActuator2()
                val container = object : ActuatorsContainer() {
                    override val context: Context = FuzzyContext
                }.apply {
                    addAll(
                        MyActuator1(),
                        MyActuator1(),
                        actuator2,
                    )
                }
                container.get<MyActuator2> { this shouldBeSameInstanceAs actuator2 }
                container.getAll<MyActuator1> { size shouldBe 2 }
            }
            test("The container, when queried with an invalid class, should not execute the lambda") {
                val container = object : ActuatorsContainer() {
                    override val context: Context = FuzzyContext
                }
                container.get<MyActuator1> {
                    failure("This lambda should not be executed since no ${MyActuator1::class} is available")
                }
            }
        }
    },
)
