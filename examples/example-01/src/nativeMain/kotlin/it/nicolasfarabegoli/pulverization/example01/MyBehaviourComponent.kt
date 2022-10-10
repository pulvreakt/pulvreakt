package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.SendReceiveDeviceComponent
import org.koin.core.component.KoinComponent

actual class MyBehaviourComponent(override val id: String) :
    SendReceiveDeviceComponent<BehaviourOutgoingMessages, BehaviourIncomingMessages, String>,
    KoinComponent {
    override fun sendToComponent(payload: BehaviourOutgoingMessages, to: String?) {
        TODO("Not yet implemented")
    }

    override fun receiveFromComponent(from: String?): BehaviourIncomingMessages {
        TODO("Not yet implemented")
    }

    override suspend fun cycle() {
        TODO("Not yet implemented")
    }
}
