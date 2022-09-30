package it.nicolasfarabegoli

import it.nicolasfarabegoli.pulverization.core.Communication

actual class NetworkManager : Communication<NetworkSend, NetworkReceive> {
    override suspend fun send(payload: NetworkSend) {
        TODO("Not yet implemented")
    }

    override suspend fun receive(): NetworkReceive {
        TODO("Not yet implemented")
    }
}
