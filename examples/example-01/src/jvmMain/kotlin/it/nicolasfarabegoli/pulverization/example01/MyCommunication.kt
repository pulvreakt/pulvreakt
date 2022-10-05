package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.Communication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyCommunication : Communication<String, String> {
    override fun send(payload: String) {
        println("Send to neighbours: $payload")
    }

    override fun receive(): String {
        return "FF"
    }
}

class MyCommunicationComponent : DeviceComponent<String, String, String>, KoinComponent {
    private val myCommunication: MyCommunication by inject()

    override fun sendToComponent(payload: String, to: String) {
        println("Send $payload to Behaviour component")
    }

    override fun receiveFromComponent(from: String): String {
        return "Fake state"
    }

    override suspend fun cycle() {
        val r = receiveFromComponent("")
        myCommunication.send(r)
        val rr = myCommunication.receive()
        sendToComponent(rr, "")
    }
}
