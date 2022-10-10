package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.SendReceiveDeviceComponent
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.BehaviourOutput
import it.nicolasfarabegoli.pulverization.core.State
import org.koin.core.component.KoinComponent

data class Export(val sensors: Map<String, Double>)

class MyBehaviour : Behaviour<String, Map<String, Export>, Unit, Unit, Unit> {
    override fun invoke(
        state: String,
        export: Map<String, Export>,
        sensedValues: Set<Unit>,
    ): BehaviourOutput<String, Map<String, Export>, Unit, Unit> {
        return BehaviourOutput(state, export, emptySet(), Unit) // Identity function
    }
}

class MyState(initialState: String) : State<String> {
    private var state: String = initialState

    override fun get(): String = state

    override fun update(newState: String): String {
        val oldState = state
        state = newState
        return oldState
    }
}

sealed class OutgoingMessages {
    data class CommunicationMessage(val export: String) : OutgoingMessages()
    data class UpdateState(val newState: String) : OutgoingMessages()
}

expect class MyBehaviourComponent : SendReceiveDeviceComponent<OutgoingMessages, Unit, String>, KoinComponent
