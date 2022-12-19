package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Represent a reference to the _communication_ component in a pulverized system.
 */
interface CommunicationRef<S : CommunicationPayload> : ComponentRef<S> {
    companion object {
        /**
         * Create a [CommunicationRef] specifying the [serializer] and the [communicator] to be used.
         */
        fun <S : CommunicationPayload> create(serializer: KSerializer<S>, communicator: Communicator):
            CommunicationRef<S> = CommunicationRefImpl(serializer, communicator)

        /**
         * Create a [CommunicationRef] specifying the [communicator] to be used.
         */
        inline fun <reified S : CommunicationPayload> create(communicator: Communicator): CommunicationRef<S> =
            create(serializer(), communicator)

        /**
         * Create a fake component reference.
         * All the methods implementation with this instance do nothing.
         */
        fun <S : CommunicationPayload> createDummy(): CommunicationRef<S> = NoOpCommunicationRef()
    }
}

internal class CommunicationRefImpl<S : CommunicationPayload>(
    private val serializer: KSerializer<S>,
    private val communicator: Communicator,
) : ComponentRef<S> by ComponentRefImpl(serializer, BehaviourComponent to CommunicationComponent, communicator),
    CommunicationRef<S>

internal class NoOpCommunicationRef<S : CommunicationPayload> :
    ComponentRef<S> by NoOpComponentRef(),
    CommunicationRef<S>
