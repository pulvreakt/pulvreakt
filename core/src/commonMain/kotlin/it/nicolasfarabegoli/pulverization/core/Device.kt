package it.nicolasfarabegoli.pulverization.core

/**
 * Models the concept of [Device] in the pulverization context.
 * @param I the type of the [Sensor]s and [Actuator]s used in the device.
 * @param S the type of the [State].
 * @param E the type of the export.
 * @param SendType the type of the message to send.
 * @param ReceiveType the type of the message to receive.
 * @param Outcome the type of the outcome of the [Behaviour].
 */
interface Device<I, S, E, SC, PC, SendType, ReceiveType, Outcome> {
    val id: I
    val sensorsContainer: SensorsContainer<I>
    val actuatorsContainer: ActuatorsContainer<I>
    val communication: Communication<SendType, ReceiveType>
    val state: State<S>
    val behaviour: Behaviour<S, E, SC, PC, Outcome>
}
