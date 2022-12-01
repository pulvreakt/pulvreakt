package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

interface SensorsRef<S : Any> : ComponentRef<S> {
    companion object {
        fun <S : Any> create(serializer: KSerializer<S>, communicator: Communicator): SensorsRef<S> =
            SensorsRefImpl(serializer, communicator)

        inline fun <reified S : Any> create(communicator: Communicator): SensorsRef<S> =
            create(serializer(), communicator)

        fun <S : Any> createDummy(): SensorsRef<S> = NoOpSensorsRef()
    }
}

internal class SensorsRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
    private val communicator: Communicator,
) : ComponentRef<S> by ComponentRefImpl(serializer, BehaviourComponent to SensorsComponent, communicator),
    SensorsRef<S>

internal class NoOpSensorsRef<S : Any> : ComponentRef<S> by NoOpComponentRef(), SensorsRef<S>
