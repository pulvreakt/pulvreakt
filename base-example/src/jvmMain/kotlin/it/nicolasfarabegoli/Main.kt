package it.nicolasfarabegoli

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.aSocket
import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val myPort: Int = System.getenv("MY_PORT")?.toInt() ?: 9000
    val neighborPort: Int = System.getenv("NEIGHBOR_PORT")?.toInt() ?: 9000
    val neighbourHost: String = System.getenv("NEIGHBOUR_HOST") ?: "127.0.0.1"

    println("Connecting to $neighbourHost - $neighborPort")

    val sm = SelectorManager(Dispatchers.IO)
    val server = aSocket(sm).tcp().bind("0.0.0.0", myPort)
    delay(2000)
    val clients = listOf(neighborPort).map { aSocket(SelectorManager(Dispatchers.IO)).tcp().connect(neighbourHost, it) }

    val network = NetworkManager(server, clients).apply { launch { initialize() } }
    val state = MyState(0)
    val behaviour = MyBehaviour()
    val device = MyDevice(
        0,
        SensorsContainer(),
        ActuatorsContainer(),
        network,
        state,
        behaviour
    )
    val platform = Platform(10, device)

    launch {
        platform.start()
    }
}
