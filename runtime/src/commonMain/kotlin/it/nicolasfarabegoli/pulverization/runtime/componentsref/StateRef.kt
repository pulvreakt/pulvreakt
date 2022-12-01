package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

interface StateRef<S : StateRepresentation> : ComponentRef<S> {
    companion object {
        fun <S : StateRepresentation> create(serializer: KSerializer<S>, communicator: Communicator): StateRef<S> =
            StateRefImpl(serializer, communicator)

        inline fun <reified S : StateRepresentation> create(communicator: Communicator): StateRef<S> =
            create(serializer(), communicator)

        fun <S : StateRepresentation> createDummy(): StateRef<S> = NoOpStateRef()
    }
}

internal data class StateRefImpl<S : StateRepresentation>(
    private val serializer: KSerializer<S>,
    private val communicator: Communicator,
) : ComponentRef<S> by ComponentRefImpl(serializer, BehaviourComponent to StateComponent, communicator),
    StateRef<S>

internal class NoOpStateRef<S : StateRepresentation> : ComponentRef<S> by NoOpComponentRef(), StateRef<S>
