package it.unibo.pulvreakt.api.component.pulverization

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.api.component.ComponentKind.Behavior
import it.unibo.pulvreakt.api.component.ComponentKind.Sensor
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.api.scheduler.ExecutionScheduler
import it.unibo.pulvreakt.errors.component.ComponentError
import kotlinx.coroutines.delay
import kotlinx.serialization.KSerializer

/**
 * Represents the Sensors component in the pulverization model.
 * @param SS represents the type handled by the sensors component.
 * This type can be a "compound" type, i.e. a type that contains all the types handled by the sensors.
 * This component is executed according to the given [executionScheduler].
 */
abstract class Sensors<ID : Any, out SS : Any>(
    private val executionScheduler: ExecutionScheduler,
    private val serializer: KSerializer<SS>,
) : AbstractPulverizedComponent<ID>() {
    /**
     * Sense the environment and return the sensed data.
     */
    abstract suspend fun sense(): SS

    final override fun getRef(): ComponentRef = ComponentRef.create(this, Sensor)

    override suspend fun execute(): Either<ComponentError, Unit> =
        either {
            val behaviourRef = getComponentByType(Behavior).bind()
            executionScheduler.timesSequence().forEach {
                val sensedData = sense()
                send(behaviourRef, sensedData, serializer).bind()
                delay(it)
            }
        }
}
