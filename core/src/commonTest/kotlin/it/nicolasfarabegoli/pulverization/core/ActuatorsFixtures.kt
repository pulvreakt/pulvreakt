package it.nicolasfarabegoli.pulverization.core

object ActuatorsFixtures {
    class MyActuator1(override val id: Int) : Actuator<Int, Int> {
        override fun actuate(payload: Int) = TODO("Not yet implemented")
    }

    data class MyPayload(val p: Double)
    class MyActuator2(override val id: Int) : Actuator<MyPayload, Int> {
        override fun actuate(payload: MyPayload) = TODO("Not yet implemented")
    }

    class DummyActuator(override val id: Int) : Actuator<Int, Int> {
        override fun actuate(payload: Int) {
            TODO("Not yet implemented")
        }
    }
}
