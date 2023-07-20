package it.unibo.pulvreakt.core.component.pulverisation.sensors

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.component.errors.ComponentError
import it.unibo.pulvreakt.core.component.pulverisation.AbstractPulverisedComponent
import it.unibo.pulvreakt.core.component.time.TimeDistribution
import kotlinx.coroutines.delay
import kotlinx.serialization.KSerializer

/**
 * Represents the Sensors component in the pulverization model.
 * @param SS represents the type handled by the sensors component.
 * This type can be a "compound" type, i.e. a type that contains all the types handled by the sensors.
 * This component is executed according to the given [timeDistribution].
 */
abstract class Sensors<SS : Any>(
    private val timeDistribution: TimeDistribution,
    private val serializer: KSerializer<SS>,
) : AbstractPulverisedComponent() {
    /**
     * Sense the environment and return the sensed data.
     */
    abstract suspend fun sense(): SS

    final override fun getRef(): ComponentRef = ComponentRef.create(this, ComponentType.Sensor)

    override suspend fun execute(): Either<ComponentError, Unit> = either {
        val behaviourRef = getComponentByType(ComponentType.Behaviour).bind()
        while (!timeDistribution.isCompleted()) {
            val sensedData = sense()
            send(behaviourRef, sensedData, serializer).bind()
            delay(timeDistribution.nextTimeInstant())
        }
    }
}
