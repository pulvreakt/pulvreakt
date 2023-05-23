package it.unibo.pulvreakt.runtime.componentsref

import it.unibo.pulvreakt.dsl.model.Actuators
import it.unibo.pulvreakt.dsl.model.Behaviour
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Represent a reference to the _actuators_ component in a pulverized system.
 */
interface ActuatorsRef<S : Any> : ComponentRef<S> {
    companion object {
        /**
         * Create a [ActuatorsRef] specifying the [serializer].
         */
        fun <S : Any> create(serializer: KSerializer<S>): ActuatorsRef<S> = ActuatorsRefImpl(serializer)

        /**
         * Create a [ActuatorsRef].
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
