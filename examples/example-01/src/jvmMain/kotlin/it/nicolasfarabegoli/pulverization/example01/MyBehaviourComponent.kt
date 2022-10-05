package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class MyBehaviourComponent(override val deviceID: String) : DeviceComponent<OutgoingMessages, Unit, String>, KoinComponent {
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
