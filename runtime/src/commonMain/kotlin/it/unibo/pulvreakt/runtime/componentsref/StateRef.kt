package it.unibo.pulvreakt.runtime.componentsref

import it.unibo.pulvreakt.dsl.model.Behaviour
import it.unibo.pulvreakt.dsl.model.State
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Represent a reference to the _state_ component in a pulverized system.
 */
interface StateRef<S : Any> : ComponentRef<S> {
    companion object {
        /**
         * Create a [StateRef] specifying the [serializer].
         */
        fun <S : Any> create(serializer: KSerializer<S>): StateRef<S> = StateRefImpl(serializer)

        /**
         * Create a [StateRef].
         */
        inline fun <reified S : Any> create(): StateRef<S> = create(serializer())

        /**
         * Create a fake component reference.
         * All the methods implementation with this instance do nothing.
         */
        fun <S : Any> createDummy(): StateRef<S> = NoOpStateRef()
    }
}

internal data class StateRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
) : ComponentRef<S> by ComponentRefImpl(serializer, Behaviour to State),
    StateRef<S>

internal class NoOpStateRef<S : Any> : ComponentRef<S> by NoOpComponentRef(), StateRef<S>