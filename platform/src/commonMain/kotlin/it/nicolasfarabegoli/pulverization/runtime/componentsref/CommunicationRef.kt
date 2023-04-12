package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Represent a reference to the _communication_ component in a pulverized system.
 */
interface CommunicationRef<S : Any> : ComponentRef<S> {
    companion object {
        /**
         * Create a [CommunicationRef] specifying the [serializer] and the [communicator] to be used.
         */
        fun <S : Any> create(serializer: KSerializer<S>): CommunicationRef<S> = CommunicationRefImpl(serializer)

        /**
         * Create a [CommunicationRef] specifying the [communicator] to be used.
         */
        inline fun <reified S : Any> create(): CommunicationRef<S> = create(serializer())

        /**
         * Create a fake component reference.
         * All the methods implementation with this instance do nothing.
         */
        fun <S : Any> createDummy(): CommunicationRef<S> = NoOpCommunicationRef()
    }
}

internal class CommunicationRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
) : ComponentRef<S> by ComponentRefImpl(serializer, Behaviour to Communication),
    CommunicationRef<S>

internal class NoOpCommunicationRef<S : Any> : ComponentRef<S> by NoOpComponentRef(), CommunicationRef<S>
