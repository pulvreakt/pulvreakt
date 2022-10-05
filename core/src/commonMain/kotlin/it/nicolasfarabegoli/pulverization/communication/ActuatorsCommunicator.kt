package it.nicolasfarabegoli.pulverization.communication

import it.nicolasfarabegoli.pulverization.core.Actuator
import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Enable the communication from the [Actuator]s to the state.
 * How to reach or communicate with the [State] component is up to the concrete class.
 * @param I the type of the identifier of the components.
 * @param R the type of the payload to receive from the [State].
 */
abstract class ActuatorsCommunicator<R, I> : KoinComponent, ReceiverCommunicator<R, I> {
    protected val actuatorsContainer: ActuatorsContainer<I> by inject()
}
