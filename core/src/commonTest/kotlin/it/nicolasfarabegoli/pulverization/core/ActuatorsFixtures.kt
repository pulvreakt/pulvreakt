package it.nicolasfarabegoli.pulverization.core

object ActuatorsFixtures {
    class MyActuator1 : Actuator<Int> {
        override fun actuate(payload: Int) = TODO("Not yet implemented")
    }

    data class MyPayload(val p: Double)
    class MyActuator2 : Actuator<MyPayload> {
        override fun actuate(payload: MyPayload) = TODO("Not yet implemented")
    }

    class DummyActuator : Actuator<Int> {
        override fun actuate(payload: Int) {
            TODO("Not yet implemented")
        }
    }
}
