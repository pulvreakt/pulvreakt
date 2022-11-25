package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.serialization.KSerializer

interface BehaviourRef<S : Any> : ComponentRef<S> {
    companion object {
        fun <S : Any> create(
            ser: KSerializer<S>,
            binding: Pair<PulverizedComponentType, PulverizedComponentType>,
            comm: Communicator,
            exists: Boolean = true,
        ): BehaviourRef<S> {
            return if (!exists) NoOpBehaviourRef()
            else BehaviourRefImpl(ser, binding, comm)
        }
    }
}

internal class BehaviourRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
    private val binding: Pair<PulverizedComponentType, PulverizedComponentType>,
    private val communicator: Communicator,
) : ComponentRef<S> by ComponentRefImpl(serializer, binding, communicator), BehaviourRef<S>

internal class NoOpBehaviourRef<S : Any> : ComponentRef<S> by NoOpComponentRef(), BehaviourRef<S>
