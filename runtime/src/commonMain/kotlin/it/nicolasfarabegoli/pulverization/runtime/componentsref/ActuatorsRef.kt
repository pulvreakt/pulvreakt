package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.serialization.KSerializer

interface ActuatorsRef<S : Any> : ComponentRef<S> {
    companion object {
        fun <S : Any> create(ser: KSerializer<S>, comm: Communicator, exists: Boolean = true): ActuatorsRef<S> {
            return if (!exists) NoOpActuatorsRef()
            else ActuatorsRefImpl(ser, comm)
        }
    }
}

internal class ActuatorsRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
    private val communicator: Communicator,
) : ComponentRef<S> by ComponentRefImpl(serializer, BehaviourComponent to ActuatorsComponent, communicator),
    ActuatorsRef<S>

internal class NoOpActuatorsRef<S : Any> : ComponentRef<S> by NoOpComponentRef(), ActuatorsRef<S>
