package it.nicolasfarabegoli.pulverization.core

interface Device<I, S : State<S>, E, SendType, ReceiveType, Outcome> {
    val sensorsContainer: SensorsContainer<I>
    val actuatorsContainer: ActuatorsContainer<I>
    val communication: Communication<SendType, ReceiveType>
    val state: State<S>
    val behaviour: Behaviour<S, E, SensorPayload, ActuatorPayload, Outcome>
}

