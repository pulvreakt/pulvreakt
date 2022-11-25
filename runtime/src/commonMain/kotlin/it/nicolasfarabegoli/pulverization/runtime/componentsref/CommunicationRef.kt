package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.serialization.KSerializer

interface CommunicationRef<S : Any> : ComponentRef<S> {
    companion object {
        fun <S : Any> create(
            serializer: KSerializer<S>,
            communicator: Communicator,
            exists: Boolean = true,
        ): ComponentRef<S> {
            return if (!exists) NoOpComponentRef()
            else CommunicationRefImpl(serializer, communicator)
        }
    }
}

internal class CommunicationRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
    private val communicator: Communicator,
) : ComponentRef<S> by ComponentRefImpl(serializer, BehaviourComponent to CommunicationComponent, communicator),
    CommunicationRef<S>

internal class NoOpCommunicationRef<S : Any> : ComponentRef<S> by NoOpComponentRef(), CommunicationRef<S>
