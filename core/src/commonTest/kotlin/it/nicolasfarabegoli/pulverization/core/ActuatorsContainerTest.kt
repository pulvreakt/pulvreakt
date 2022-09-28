package it.nicolasfarabegoli.pulverization.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer.Companion.getActuator

class ActuatorsContainerTest : FunSpec({
    context("ActuatorsContainer tests") {
        test("The container when queried with an `Actuator` type should give the right instance") {
            class MyActuator1(override val id: Int) : Actuator<ActuatorPayload, Int> {
                override fun actuate(payload: ActuatorPayload) = TODO("Not yet implemented")
            }

            abstract class MyPayload : ActuatorPayload
            class MyActuator2(override val id: Int) : Actuator<MyPayload, Int> {
                override fun actuate(payload: MyPayload) = TODO("Not yet implemented")
            }

            class MyContainer : ActuatorsContainer<Int> {
                override var actuators: Set<Actuator<ActuatorPayload, Int>> = setOf()

                override fun <A : Actuator<ActuatorPayload, Int>> addActuator(actuator: A) {
                    actuators += actuator
                }
            }

            val container = MyContainer()
            val actuator = MyActuator1(10)
            container.addActuator(actuator)
            container.getActuators(MyActuator1::class).size shouldBe 1
            container.getActuator<Int, ActuatorPayload, MyActuator1>()?.let {
                it shouldBe actuator
            }
            container.getActuator(MyActuator2::class) shouldBe null
        }
    }
})
