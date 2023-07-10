package it.unibo.pulvreakt.core.component.pulverisation

import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.ComponentType

/**
 * Represents the _Actuators_ component in the pulverization model.
 * @param AS represents the type handled by the actuators component.
 * This type can be a "compound" type, i.e. a type that contains all the types handled by the actuators.
 */
abstract class Actuators<AS : Any> : AbstractComponent() {
    /**
     * Actuate the actuators with the given [actuation].
     */
    abstract suspend fun actuate(actuation: AS)

    final override fun getRef(): ComponentRef = ComponentRef.create(this, ComponentType.Actuator)
}
