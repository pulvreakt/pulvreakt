package it.nicolasfarabegoli.pulverization.core

object SensorsFixtures {
    class MySensor1(override val id: Int) : Sensor<SensorPayload, Int> {
        override fun sense(): SensorPayload {
            TODO("Not yet implemented")
        }
    }

    class MySensor2(override val id: Int) : Sensor<SensorPayload, Int> {
        override fun sense(): SensorPayload {
            TODO("Not yet implemented")
        }
    }

    data class MyPayload(val p: Double) : SensorPayload
    class DummySensor(override val id: Int) : Sensor<MyPayload, Int> {
        override fun sense(): MyPayload {
            TODO("Not yet implemented")
        }
    }
}
