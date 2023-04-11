package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
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
            toComponent: ComponentType,
        ): BehaviourRef<S> = BehaviourRefImpl(serializer, toComponent)

        /**
         * Create a new [BehaviourRef] specifying the other [toComponent] to communicate with and a
         * [communicator] to be used to communicate with.
         */
        inline fun <reified S : Any> create(
            toComponent: ComponentType,
        ): BehaviourRef<S> = create(serializer(), toComponent)
    }
}

internal class BehaviourRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
    private val toComponent: ComponentType,
) : ComponentRef<S> by ComponentRefImpl(serializer, toComponent to Behaviour), BehaviourRef<S>
