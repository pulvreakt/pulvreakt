package it.unibo.pulvreakt.core.component.pulverisation

import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.ComponentType

/**
 * Represents the Behaviour component in the pulverization model.
 */
abstract class Behaviour<State : Any, Comm : Any, Sensors : Any, Actuators : Any> : AbstractComponent() {
    /**
     * Execute the behaviour logic with the given [state], [comm], [sensors] and [actuators].
     * @return the new state, the communication to be sent and the prescriptive actions to be performed.
     */
    abstract operator fun invoke(state: State, comm: List<Comm>, sensors: Sensors, actuators: Actuators): BehaviourOutput<State, Comm, Actuators>

    final override fun getRef(): ComponentRef = ComponentRef.create(this, ComponentType.Behaviour)
}

/**
 * Models the output of a [Behaviour] where [state] is the new state of the component,
 * [comm] is the communication to be sent to the other devices
 * and [actuators] is the prescriptive actions to be performed.
 */
data class BehaviourOutput<State : Any, Comm : Any, Actuators : Any>(
    val state: State,
    val comm: Comm,
    val actuators: Actuators,
)
