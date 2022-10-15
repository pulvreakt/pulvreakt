package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.SendReceiveDeviceComponent
import it.nicolasfarabegoli.pulverization.core.Communication
import org.koin.core.component.KoinComponent

actual class MyCommunication(override val id: String) : Communication<Export, List<Export>, String> {
    override fun send(payload: Export) {
        TODO("Not yet implemented")
    }

    override fun receive(): List<Export> {
        TODO("Not yet implemented")
    }
}

actual class MyCommunicationComponent(override val id: String) :
    SendReceiveDeviceComponent<List<Export>, Export, String>, KoinComponent {
    override fun sendToComponent(payload: List<Export>, to: String?) {
        TODO("Not yet implemented")
    }

    override fun receiveFromComponent(from: String?): Export {
        TODO("Not yet implemented")
    }

    override suspend fun cycle() {
        TODO("Not yet implemented")
    }
}
