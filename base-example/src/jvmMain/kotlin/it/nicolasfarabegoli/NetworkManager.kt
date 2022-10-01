package it.nicolasfarabegoli

import io.ktor.network.sockets.ServerSocket
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.readUTF8Line
import io.ktor.utils.io.writeStringUtf8
import it.nicolasfarabegoli.pulverization.core.Communication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

actual class NetworkManager(
    private val server: ServerSocket,
    private val clients: List<Socket>,
) : Communication<NetworkSend, NetworkReceive> {

    private lateinit var socket: Socket
    private var receivedMessages: Map<String, String> = emptyMap()
    private var clientSockets: List<ByteWriteChannel> = emptyList()

    suspend fun finalize() = coroutineScope {
        withContext(Dispatchers.IO) {
            socket.close()
        }
    }

    suspend fun initialize() = coroutineScope {
        println("Initializing")
        clientSockets = clients.map { it.openWriteChannel(autoFlush = true) }
        launch {
            while (true) {
                socket = server.accept()
                val receiveChannel = socket.openReadChannel()
                try {
                    while (true) {
                        receiveChannel.readUTF8Line()?.let {
                            receivedMessages += socket.remoteAddress.toString() to it
                        }
                    }
                } catch (e: Throwable) {
                    withContext(Dispatchers.IO) {
                        socket.close()
                    }
                }
            }
        }
    }

    override suspend fun send(payload: NetworkSend): Unit = coroutineScope {
        launch {
            clientSockets.forEach { it.writeStringUtf8(payload.payload) }
        }
    }

    override suspend fun receive(): NetworkReceive {
        return NetworkReceive(receivedMessages.values.toList())
    }
}
