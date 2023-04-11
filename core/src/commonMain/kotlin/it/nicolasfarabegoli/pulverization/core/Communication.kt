package it.nicolasfarabegoli.pulverization.core

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import kotlinx.coroutines.flow.Flow

/**
 * Marker interface modelling the export payload.
 */
// interface CommunicationPayload

/**
 * This interface model the _Communication component_ in a pulverized context.
 * This component is responsible for providing the operations needed to communicate with other devices.
 * @param S the type of the message to send.
 * @param R the type of the message to receive.
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
