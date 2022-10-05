package it.nicolasfarabegoli.pulverization.communication

import it.nicolasfarabegoli.pulverization.core.Sensor
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Enable the communication from the [Sensor]s to the state.
 * How to reach or communicate with the [State] component is up to the concrete class.
 * @param I the type of the identifier of the components.
 * @param P the type of the payload to send to the [State].
 */
abstract class SensorsCommunicator<P, I> : KoinComponent, SenderCommunicator<P, I> {
    protected val sensorsContainer: SensorsContainer<I> by inject()
}
