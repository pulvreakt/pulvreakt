package it.nicolasfarabegoli.pulverization.core

import it.nicolasfarabegoli.pulverization.core.DeviceIDOps.IntID

object SensorsFixtures {
    class MySensor1(override val id: IntID) : Sensor<Int, IntID> {
        override fun sense(): Int {
            TODO("Not yet implemented")
        }
    }

    class MySensor2(override val id: IntID) : Sensor<Int, IntID> {
        override fun sense(): Int {
            TODO("Not yet implemented")
        }
    }

    data class MyPayload(val p: Double)
    class DummySensor(override val id: IntID) : Sensor<MyPayload, IntID> {
        override fun sense(): MyPayload {
            TODO("Not yet implemented")
        }
    }
}
