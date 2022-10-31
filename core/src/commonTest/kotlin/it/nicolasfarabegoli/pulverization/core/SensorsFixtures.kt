package it.nicolasfarabegoli.pulverization.core

object SensorsFixtures {
    class MySensor1 : Sensor<Int> {
        override fun sense(): Int {
            TODO("Not yet implemented")
        }
    }

    class MySensor2 : Sensor<Int> {
        override fun sense(): Int {
            TODO("Not yet implemented")
        }
    }

    data class MyPayload(val p: Double)
    class DummySensor : Sensor<MyPayload> {
        override fun sense(): MyPayload {
            TODO("Not yet implemented")
        }
    }
}
