package it.nicolasfarabegoli.pulverization.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import it.nicolasfarabegoli.pulverization.core.SensorsContainer.Companion.getSensor
import it.nicolasfarabegoli.pulverization.core.SensorsFixtures.DummySensor
import it.nicolasfarabegoli.pulverization.core.SensorsFixtures.MySensor1
import it.nicolasfarabegoli.pulverization.core.SensorsFixtures.MySensor2

class SensorsContainerTest : FunSpec({
    context("SensorsContainer test") {
        test("The container when queried with a `Sensor` type should give the right instance") {
            val mySensor1 = MySensor1(10)
            val container = SensorsContainer<Int>().apply {
                addSensor(MySensor2(11))
                addSensor(MySensor2(12))
                addSensor(mySensor1)
            }

            container.getSensor(MySensor1::class) shouldNotBe null
            container.getSensor(MySensor1::class)?.let { it shouldBeSameInstanceAs mySensor1 }
            container.getSensors(MySensor1::class).size shouldBe 1
            container.getSensors(MySensor2::class).size shouldBe 2
            container.getSensor(DummySensor::class) shouldBe null
            container.getSensors(DummySensor::class).size shouldBe 0
        }

        test("An empty container should return empty set or null") {
            val container = SensorsContainer<Int>()

            container.getSensor<Int, SensorPayload, MySensor1>() shouldBe null
            container.getSensors(MySensor2::class).size shouldBe 0
        }
    }
})
