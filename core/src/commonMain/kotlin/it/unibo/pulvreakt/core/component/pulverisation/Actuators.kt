package it.unibo.pulvreakt.core.component.pulverisation

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.component.errors.ComponentError
import kotlinx.serialization.KSerializer

/**
 * Represents the _Actuators_ component in the pulverization model.
 * @param AS represents the type handled by the actuators component.
 * This type can be a "compound" type, i.e. a type that contains all the types handled by the actuators.
 */
abstract class Actuators<AS : Any>(
    private val serializer: KSerializer<AS>,
) : AbstractPulverisedComponent() {
    /**
     * Actuate the actuators with the given [actuation].
     */
    abstract suspend fun actuate(actuation: AS)

    final override fun getRef(): ComponentRef = ComponentRef.create(this, ComponentType.Actuator)

    override suspend fun execute(): Either<ComponentError, Unit> = either {
        val behaviourRef = getComponentByType(ComponentType.Behaviour).bind()
        receive(behaviourRef, serializer).bind().collect { actuate(it) }
    }
}
