package it.unibo.pulvreakt.runtime.componentsref

import it.unibo.pulvreakt.dsl.model.Behaviour
import it.unibo.pulvreakt.dsl.model.Sensors
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Represent a reference to the _sensors_ component in a pulverized system.
 */
interface SensorsRef<S : Any> : ComponentRef<S> {
    companion object {
        /**
         * Create a [SensorsRef] specifying the [serializer].
         */
        fun <S : Any> create(serializer: KSerializer<S>): SensorsRef<S> = SensorsRefImpl(serializer)

        /**
         * Create a [SensorsRef].
         */
        inline fun <reified S : Any> create(): SensorsRef<S> = create(serializer())

        /**
         * Create a fake component reference.
         * All the methods implementation with this instance do nothing.
         */
        fun <S : Any> createDummy(): SensorsRef<S> = NoOpSensorsRef()
    }
}

internal class SensorsRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
) : ComponentRef<S> by ComponentRefImpl(serializer, Behaviour to Sensors), SensorsRef<S>

internal class NoOpSensorsRef<S : Any> : ComponentRef<S> by NoOpComponentRef(), SensorsRef<S>