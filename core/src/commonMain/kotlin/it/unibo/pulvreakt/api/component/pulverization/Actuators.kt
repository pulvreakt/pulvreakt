package it.unibo.pulvreakt.api.component.pulverization

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.api.component.ComponentKind
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.errors.component.ComponentError
import kotlinx.serialization.KSerializer

/**
 * Represents the _Actuators_ component in the pulverization model.
 * @param AS represents the type handled by the actuators component.
 * This type can be a "compound" type, i.e. a type that contains all the types handled by the actuators.
 */
abstract class Actuators<ID : Any, in AS : Any>(private val serializer: KSerializer<AS>) : AbstractPulverizedComponent<ID>() {
    /**
     * Actuate the actuators with the given [actuation].
     */
    abstract suspend fun actuate(actuation: AS)

    final override fun getRef(): ComponentRef = ComponentRef.create(this, ComponentKind.Actuator)

    override suspend fun execute(): Either<ComponentError, Unit> = either {
        val behaviourRef = getComponentByType(ComponentKind.Behavior).bind()
        receive(behaviourRef, serializer).bind().collect { actuate(it) }
    }
}
