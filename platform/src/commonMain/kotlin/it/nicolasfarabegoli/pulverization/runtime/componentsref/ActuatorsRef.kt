package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.dsl.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.model.Behaviour
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
        fun <S : Any> create(serializer: KSerializer<S>): ActuatorsRef<S> = ActuatorsRefImpl(serializer)

        /**
         * Create a [ActuatorsRef] specifying the [communicator] to be used.
         */
        inline fun <reified S : Any> create(): ActuatorsRef<S> = create(serializer())

        /**
         * Create a fake component reference.
         * All the methods implementation with this instance do nothing.
         */
        fun <S : Any> createDummy(): ActuatorsRef<S> = NoOpActuatorsRef()
    }
}

internal class ActuatorsRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
) : ComponentRef<S> by ComponentRefImpl(serializer, Behaviour to Actuators),
    ActuatorsRef<S>

internal class NoOpActuatorsRef<S : Any> : ComponentRef<S> by NoOpComponentRef(), ActuatorsRef<S>
