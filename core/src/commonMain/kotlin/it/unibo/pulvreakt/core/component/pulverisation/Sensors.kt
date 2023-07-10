package it.unibo.pulvreakt.core.component.pulverisation

import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.ComponentType

/**
 * Represents the Sensors component in the pulverization model.
 * @param SS represents the type handled by the sensors component.
 * This type can be a "compound" type, i.e. a type that contains all the types handled by the sensors.
 */
abstract class Sensors<SS : Any> : AbstractComponent() {
    /**
     * Sense the environment and return the sensed data.
     */
    abstract suspend fun sense(): SS

    final override fun getRef(): ComponentRef = ComponentRef.create(this, ComponentType.Sensor)
}
