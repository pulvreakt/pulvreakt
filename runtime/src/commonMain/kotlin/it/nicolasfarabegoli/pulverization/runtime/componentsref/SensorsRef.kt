package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.serialization.KSerializer

interface SensorsRef<S : Any> : ComponentRef<S> {
    companion object {
        fun <S : Any> create(
            serializer: KSerializer<S>,
            communicator: Communicator,
            exists: Boolean = true,
        ): SensorsRef<S> {
            return if (!exists) NoOpSensorsRef()
            else SensorsRefImpl(serializer, communicator)
        }
    }
}

internal class SensorsRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
    private val communicator: Communicator,
) : ComponentRef<S> by ComponentRefImpl(serializer, BehaviourComponent to SensorsComponent, communicator),
    SensorsRef<S>

internal class NoOpSensorsRef<S : Any> : ComponentRef<S> by NoOpComponentRef(), SensorsRef<S>
