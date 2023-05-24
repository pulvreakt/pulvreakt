package it.unibo.pulvreakt.core

import it.unibo.pulvreakt.dsl.model.Communication
import it.unibo.pulvreakt.dsl.model.ComponentType
import kotlinx.coroutines.flow.Flow

/**
 * This interface model the _Communication component_ in a pulverized context.
 * This component is responsible for providing the operations needed to communicate with other devices.
 * @param P the type of the message to send.
 */
interface Communication<P : Any> : PulverizedComponent<Any, P, Any, Any, Any> {
    override val componentType: ComponentType
        get() = Communication

    /**
     * Abstraction of the _sending action_ to other devices.
     */
    suspend fun send(payload: P)

    /**
     * Abstraction of the _receiving action_ from other devices.
     */
    fun receive(): Flow<P>
}