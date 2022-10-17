package it.nicolasfarabegoli.pulverization.core

import it.nicolasfarabegoli.pulverization.core.DeviceIDOps.IntID

object ActuatorsFixtures {
    class MyActuator1(override val id: IntID) : Actuator<Int, IntID> {
        override fun actuate(payload: Int) = TODO("Not yet implemented")
    }

    data class MyPayload(val p: Double)
    class MyActuator2(override val id: IntID) : Actuator<MyPayload, IntID> {
        override fun actuate(payload: MyPayload) = TODO("Not yet implemented")
    }

    class DummyActuator(override val id: IntID) : Actuator<Int, IntID> {
        override fun actuate(payload: Int) {
            TODO("Not yet implemented")
        }
    }
}
