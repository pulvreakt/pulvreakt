package it.nicolasfarabegoli

import it.nicolasfarabegoli.pulverization.core.*
import it.nicolasfarabegoli.pulverization.platform.PulverizationPlatform

class Platform(
    override val repetitions: Int,
    override val device: Device<Int, Int, Unit, NetworkSend, NetworkReceive, Unit>,
) : PulverizationPlatform<Int, Int, Unit, NetworkSend, NetworkReceive, Unit> {

    override fun actuation(state: State<Int>, actuators: ActuatorsContainer<Int>) {
        return
    }

    override suspend fun dataPropagation(state: State<Int>, communication: suspend (payload: NetworkSend) -> Unit) {
        println("Send message")
        communication(NetworkSend("Counter: ${state.state}\n"))
    }

    override fun computation(
        state: State<Int>,
        behaviour: Behaviour<Int, Unit, SensorPayload, ActuatorPayload, Unit>,
    ) {
        behaviour(state, Unit, emptySet())
    }

    override suspend fun contextAcquisition(
        state: State<Int>,
        sensors: SensorsContainer<Int>,
        communication: suspend () -> NetworkReceive,
    ) {
        val receivedMessages = communication()
        println("Received: ${receivedMessages.payload}")
    }
}
