package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.SendReceiveDeviceComponent
import it.nicolasfarabegoli.pulverization.core.Communication
import org.koin.core.component.KoinComponent

actual class MyCommunication : Communication<String, Map<String, String>> {
    override fun send(payload: String) {
        TODO("Not yet implemented")
    }

    override fun receive(): Map<String, String> {
        TODO("Not yet implemented")
    }
}

actual class MyCommunicationComponent(override val deviceID: String) :
    SendReceiveDeviceComponent<Map<String, String>, String, String>, KoinComponent {
    override fun sendToComponent(payload: Map<String, String>, to: String) {
        TODO("Not yet implemented")
    }

    override fun receiveFromComponent(from: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun cycle() {
        TODO("Not yet implemented")
    }
}
