package it.nicolasfarabegoli.pulverization.core

object ActuatorsFixtures {
    class MyActuator1(override val id: Int) : Actuator<ActuatorPayload, Int> {
        override fun actuate(payload: ActuatorPayload) = TODO("Not yet implemented")
    }

    data class MyPayload(val p: Double) : ActuatorPayload
    class MyActuator2(override val id: Int) : Actuator<MyPayload, Int> {
        override fun actuate(payload: MyPayload) = TODO("Not yet implemented")
    }

    class DummyActuator(override val id: Int) : Actuator<ActuatorPayload, Int> {
        override fun actuate(payload: ActuatorPayload) {
            TODO("Not yet implemented")
        }
    }
}