package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.SendReceiveDeviceComponent
import org.koin.core.component.KoinComponent

actual class MyBehaviourComponent(override val deviceID: String) : SendReceiveDeviceComponent<OutgoingMessages, Unit, String>, KoinComponent {
    override fun sendToComponent(payload: OutgoingMessages, to: String) {
        TODO("Not yet implemented")
    }

    override fun receiveFromComponent(from: String) {
        TODO("Not yet implemented")
    }

    override suspend fun cycle() {
        TODO("Not yet implemented")
    }
}
