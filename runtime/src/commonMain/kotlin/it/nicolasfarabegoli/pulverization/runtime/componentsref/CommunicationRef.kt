package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.serialization.KSerializer

interface CommunicationRef<S : CommunicationPayload> : ComponentRef<S> {
    companion object {
        fun <S : CommunicationPayload> create(serializer: KSerializer<S>, communicator: Communicator):
            CommunicationRef<S> = CommunicationRefImpl(serializer, communicator)

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
