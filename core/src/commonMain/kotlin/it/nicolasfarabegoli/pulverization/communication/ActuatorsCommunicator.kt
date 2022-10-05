package it.nicolasfarabegoli.pulverization.communication

import it.nicolasfarabegoli.pulverization.core.Actuator
import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.Behaviour
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Enable the communication from the [Actuator]s to the state.
 * How to reach (or communicate with) the [Behaviour] component is up to the concrete class.
 * @param I the type of the identifier of the components.
 * @param R the type of the payload to receive from the [Behaviour].
 */
abstract class ActuatorsCommunicator<R, I> : KoinComponent, ReceiverCommunicator<R, I> {
    protected val actuatorsContainer: ActuatorsContainer<I> by inject()
}
