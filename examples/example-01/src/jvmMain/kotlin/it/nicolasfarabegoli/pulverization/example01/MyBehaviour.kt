package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.BehaviourOutput
import it.nicolasfarabegoli.pulverization.core.State
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyBehaviour : Behaviour<String, String, Unit, Unit, Unit> {
    override fun invoke(state: String, export: String, sensedValues: Set<Unit>): BehaviourOutput<String, String, Unit, Unit> {
        return BehaviourOutput(state, export, emptySet(), Unit) // Identity function
    }
}

class MyState : State<String> {
    private var state: String = "MyState"

    override fun get(): String = state

    override fun update(newState: String): String {
        val oldState = state
        state = newState
        return oldState
    }
}

sealed class OutgoingMessages {
    data class SendMyState(val state: String) : OutgoingMessages()
}

class MyBehaviourComponent(override val deviceID: String) : DeviceComponent<OutgoingMessages, Unit, String>, KoinComponent {
    private val state: MyState by inject()
    private val behaviour: MyBehaviour by inject()

    override fun sendToComponent(payload: OutgoingMessages, to: String) {
        when (payload) {
            is OutgoingMessages.SendMyState -> println(payload.state)
        }
    }

    override fun receiveFromComponent(from: String) { }

    override suspend fun cycle() {
        val result = behaviour(state.get(), "", emptySet()) // How destructure the result like Scala?
        sendToComponent(OutgoingMessages.SendMyState(result.newState), "")
    }
}
