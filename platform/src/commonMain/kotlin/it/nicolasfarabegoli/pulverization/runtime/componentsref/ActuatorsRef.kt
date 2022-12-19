package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Represent a reference to the _actuators_ component in a pulverized system.
 */
interface ActuatorsRef<S : Any> : ComponentRef<S> {
    companion object {
        /**
         * Create a [ActuatorsRef] specifying the [serializer] and the [communicator] to be used.
         */
        fun <S : Any> create(serializer: KSerializer<S>, communicator: Communicator): ActuatorsRef<S> =
            ActuatorsRefImpl(serializer, communicator)

        /**
         * Create a [ActuatorsRef] specifying the [communicator] to be used.
         */
        inline fun <reified S : Any> create(communicator: Communicator): ActuatorsRef<S> =
            create(serializer(), communicator)

        /**
         * Create a fake component reference.
         * All the methods implementation with this instance do nothing.
         */
        fun <S : Any> createDummy(): ActuatorsRef<S> = NoOpActuatorsRef()
    }
}

internal class ActuatorsRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
    private val communicator: Communicator,
) : ComponentRef<S> by ComponentRefImpl(serializer, BehaviourComponent to ActuatorsComponent, communicator),
    ActuatorsRef<S>

internal class NoOpActuatorsRef<S : Any> : ComponentRef<S> by NoOpComponentRef(), ActuatorsRef<S>
