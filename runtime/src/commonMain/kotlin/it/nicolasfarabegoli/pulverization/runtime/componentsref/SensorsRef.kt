package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Represent a reference to the _sensors_ component in a pulverized system.
 */
interface SensorsRef<S : Any> : ComponentRef<S> {
    companion object {
        /**
         * Create a [SensorsRef] specifying the [serializer] and the [communicator] to be used.
         */
        fun <S : Any> create(serializer: KSerializer<S>, communicator: Communicator): SensorsRef<S> =
            SensorsRefImpl(serializer, communicator)

        /**
         * Create a [SensorsRef] specifying the [communicator] to be used.
         */
        inline fun <reified S : Any> create(communicator: Communicator): SensorsRef<S> =
            create(serializer(), communicator)

        /**
         * Create a fake component reference.
         * All the methods implementation with this instance do nothing.
         */
        fun <S : Any> createDummy(): SensorsRef<S> = NoOpSensorsRef()
    }
}

internal class SensorsRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
    private val communicator: Communicator,
) : ComponentRef<S> by ComponentRefImpl(serializer, BehaviourComponent to SensorsComponent, communicator),
    SensorsRef<S>

internal class NoOpSensorsRef<S : Any> : ComponentRef<S> by NoOpComponentRef(), SensorsRef<S>
