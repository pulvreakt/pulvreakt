package it.unibo.pulvreakt.api.component.pulverization

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.api.component.ComponentKind.Behavior
import it.unibo.pulvreakt.api.component.ComponentKind.Sensor
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.scheduler.TimeDistribution
import it.unibo.pulvreakt.errors.component.ComponentError
import kotlinx.coroutines.delay
import kotlinx.serialization.KSerializer

/**
 * Represents the Sensors component in the pulverization model.
 * @param SS represents the type handled by the sensors component.
 * This type can be a "compound" type, i.e. a type that contains all the types handled by the sensors.
 * This component is executed according to the given [timeDistribution].
 */
abstract class Sensors<out SS : Any>(
    private val timeDistribution: TimeDistribution,
    private val serializer: KSerializer<SS>,
) : AbstractPulverizedComponent() {
    /**
     * Sense the environment and return the sensed data.
     */
    abstract suspend fun sense(): SS

    final override fun getRef(): ComponentRef = ComponentRef.create(this, Sensor)

    override suspend fun execute(): Either<ComponentError, Unit> = either {
        val behaviourRef = getComponentByType(Behavior).bind()
        while (!timeDistribution.isCompleted()) {
            val sensedData = sense()
            send(behaviourRef, sensedData, serializer).bind()
            delay(timeDistribution.nextTimeInstant())
        }
    }
}
