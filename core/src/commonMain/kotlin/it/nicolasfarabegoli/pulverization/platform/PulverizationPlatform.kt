package it.nicolasfarabegoli.pulverization.platform

import it.nicolasfarabegoli.pulverization.core.*

/**
 * Represent the platform in which a [Device] runs.
 */
interface PulverizationPlatform<I, S : State<S>, E, SendType, ReceiveType, Outcome> {
    val repetitions: Int
    val device: Device<I, S, E, SendType, ReceiveType, Outcome>

    fun contextAcquisition(sensors: SensorsContainer<I>, communication: () -> ReceiveType)
    fun computation(state: State<S>, behaviour: Behaviour<S, E, SensorPayload, ActuatorPayload, Outcome>)
    fun dataPropagation(state: State<S>, communication: (payload: SendType) -> Unit)
    fun actuation(state: State<S>, actuators: ActuatorsContainer<I>)

    private fun cycle() {
        contextAcquisition(device.sensorsContainer, device.communication::receive)
        computation(device.state, device.behaviour)
        dataPropagation(device.state, device.communication::send)
        actuation(device.state, device.actuatorsContainer)
    }

    suspend fun start() {
        repeat(repetitions) { cycle() }
    }

    suspend fun infiniteStart() {
        while (true) { cycle() }
    }
}
