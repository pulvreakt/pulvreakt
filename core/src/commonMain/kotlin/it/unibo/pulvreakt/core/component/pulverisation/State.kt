package it.unibo.pulvreakt.core.component.pulverisation

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.component.errors.ComponentError
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
sealed interface StateOps<StateRepr : Any> {
    @Serializable
    data class GetState<Q, StateRepr : Any>(val query: Q?) : StateOps<StateRepr>

    @Serializable
    data class SetState<StateRepr : Any>(val content: StateRepr) : StateOps<StateRepr>
}

/**
 * Represents the State component in the pulverization model.
 */
abstract class State<StateRepr : Any>(
    private val serializer: KSerializer<StateOps<StateRepr>>,
) : AbstractPulverisedComponent() {
    /**
     * Queries the state of the component.
     */
    abstract fun queryState(query: StateOps<StateRepr>): StateRepr

    final override fun getRef(): ComponentRef = ComponentRef.create(this, ComponentType.State)

    override suspend fun execute(): Either<ComponentError, Unit> = either {
        val behaviourRef = getComponentByType(ComponentType.Behaviour).bind()
        val receivingFlow = receive(behaviourRef, serializer).bind()
        receivingFlow.collect { ops -> queryState(ops) }
    }
}
