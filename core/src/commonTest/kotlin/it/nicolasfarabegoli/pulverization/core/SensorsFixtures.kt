package it.nicolasfarabegoli.pulverization.core

object SensorsFixtures {
    class MySensor1(override val id: Int) : Sensor<Int, Int> {
        override fun sense(): Int {
            TODO("Not yet implemented")
        }
    }

    class MySensor2(override val id: Int) : Sensor<Int, Int> {
        override fun sense(): Int {
            TODO("Not yet implemented")
        }
    }

    data class MyPayload(val p: Double)
    class DummySensor(override val id: Int) : Sensor<MyPayload, Int> {
        override fun sense(): MyPayload {
            TODO("Not yet implemented")
        }
    }
}
