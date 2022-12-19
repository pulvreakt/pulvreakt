package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Represent a reference to the _behaviour_ component in a pulverized system.
 */
interface BehaviourRef<S : Any> : ComponentRef<S> {
    companion object {
        /**
         * Create a new [BehaviourRef] specifying a [serializer], the other [toComponent] to communicate with and a
         * [communicator] to be used to communicate with.
         */
        fun <S : Any> create(
            serializer: KSerializer<S>,
            toComponent: PulverizedComponentType,
            communicator: Communicator,
        ): BehaviourRef<S> = BehaviourRefImpl(serializer, toComponent, communicator)

        /**
         * Create a new [BehaviourRef] specifying the other [toComponent] to communicate with and a
         * [communicator] to be used to communicate with.
         */
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
