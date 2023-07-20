package it.unibo.pulvreakt.core.component.pulverisation

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.component.errors.ComponentError
import it.unibo.pulvreakt.core.component.pulverisation.AbstractPulverisedComponent
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Represents the operations that can be performed on a [State] component.
 */
@Serializable(with = StateOpsSerializer::class)
sealed interface StateOps<out StateRepr : Any>

/**
 * Represents the operation of querying the state of a [State] component.
 */
@Serializable
object GetState : StateOps<Nothing>

/**
 * Represents the operation of setting the state of a [State] component with a new [content].
 */
@Serializable
data class SetState<StateRepr : Any>(val content: StateRepr) : StateOps<StateRepr>

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

    override suspend fun execute(): Either<ComponentError, Unit> = coroutineScope {
        either {
            val behaviourRef = getComponentByType(ComponentType.Behaviour).bind()
            val receivingFlow = receive(behaviourRef, serializer).bind()
            receivingFlow.collect { ops ->
                val queryResult = queryState(ops)
                send(behaviourRef, SetState(queryResult), serializer).bind()
            }
        }
    }
}

internal class StateOpsSerializer<StateRepr : Any>(
    stateSerializer: KSerializer<StateRepr>,
) : KSerializer<StateOps<StateRepr>> {

    @Serializable
    data class StateOpsRepr<StateRepr : Any>(
        val type: Type,
        val content: StateRepr?,
    ) {
        enum class Type {
            GetState,
            SetState,
        }
    }

    private val surrogateSerializer = StateOpsRepr.serializer(stateSerializer)

    override fun deserialize(decoder: Decoder): StateOps<StateRepr> {
        val surrogate = surrogateSerializer.deserialize(decoder)
        return when (surrogate.type) {
            StateOpsRepr.Type.GetState -> GetState
            StateOpsRepr.Type.SetState -> {
                requireNotNull(surrogate.content) { "SetState content cannot be null" }
                SetState(surrogate.content)
            }
        }
    }

    override val descriptor: SerialDescriptor = surrogateSerializer.descriptor

    override fun serialize(encoder: Encoder, value: StateOps<StateRepr>) {
        val surrogate: StateOpsRepr<StateRepr> = when (value) {
            is GetState -> StateOpsRepr(StateOpsRepr.Type.GetState, null)
            is SetState -> StateOpsRepr(StateOpsRepr.Type.SetState, value.content)
        }
        surrogateSerializer.serialize(encoder, surrogate)
    }
}
