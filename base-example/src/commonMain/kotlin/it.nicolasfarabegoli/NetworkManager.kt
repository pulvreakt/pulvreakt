package it.nicolasfarabegoli

import it.nicolasfarabegoli.pulverization.core.Communication

expect class NetworkManager : Communication<NetworkSend, NetworkReceive>

data class NetworkSend(val payload: String)
data class NetworkReceive(val payload: List<String>)
