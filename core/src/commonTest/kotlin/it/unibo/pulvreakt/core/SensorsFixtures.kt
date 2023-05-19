package it.unibo.pulvreakt.core

object SensorsFixtures {
    class MySensor1 : Sensor<Int> {
        override suspend fun sense(): Int {
            TODO("Not yet implemented")
        }
    }

    class MySensor2 : Sensor<Int> {
        override suspend fun sense(): Int {
            TODO("Not yet implemented")
        }
    }

    data class MyPayload(val p: Double)
    class DummySensor : Sensor<MyPayload> {
        override suspend fun sense(): MyPayload {
            TODO("Not yet implemented")
        }
    }
}
