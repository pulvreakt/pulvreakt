package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.BehaviourOutput
import it.nicolasfarabegoli.pulverization.core.State
import org.koin.core.component.KoinComponent

class MyBehaviour : Behaviour<String, String, Unit, Unit, Unit> {
    override fun invoke(state: String, export: String, sensedValues: Set<Unit>): BehaviourOutput<String, String, Unit, Unit> {
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
    data class CommunicationMessage(val state: String) : OutgoingMessages()
}

expect class MyBehaviourComponent : DeviceComponent<OutgoingMessages, Unit, String>, KoinComponent
