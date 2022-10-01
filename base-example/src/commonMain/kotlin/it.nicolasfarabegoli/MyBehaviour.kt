package it.nicolasfarabegoli

import it.nicolasfarabegoli.pulverization.core.*

class MyBehaviour : Behaviour<Int, Unit, SensorPayload, ActuatorPayload, Unit> {
    override fun invoke(
        state: State<Int>,
        export: Unit,
        sensedValues: Set<SensorPayload>,
    ): BehaviourOutput<Int, Unit, ActuatorPayload, Unit> {
        state.state += 1
        println("State updated: ${state.state}")
        return BehaviourOutput(state, Unit, emptySet(), Unit)
    }
}
