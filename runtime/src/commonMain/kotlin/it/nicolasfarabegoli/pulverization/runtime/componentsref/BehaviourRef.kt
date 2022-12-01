package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

interface BehaviourRef<S : Any> : ComponentRef<S> {
    companion object {
        fun <S : Any> create(
            ser: KSerializer<S>,
            toComponent: PulverizedComponentType,
            communicator: Communicator,
        ): BehaviourRef<S> = BehaviourRefImpl(ser, toComponent, communicator)

        inline fun <reified S : Any> create(
            toComponent: PulverizedComponentType,
            communicator: Communicator,
        ): BehaviourRef<S> = create(serializer(), toComponent, communicator)
    }
}

internal class BehaviourRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
    private val toComponent: PulverizedComponentType,
    private val communicator: Communicator,
) : ComponentRef<S> by ComponentRefImpl(serializer, toComponent to BehaviourComponent, communicator), BehaviourRef<S>
