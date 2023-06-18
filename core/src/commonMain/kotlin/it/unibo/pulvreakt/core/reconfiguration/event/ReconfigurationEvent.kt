package it.unibo.pulvreakt.core.reconfiguration.event

import it.unibo.pulvreakt.core.component.Initializable
import kotlinx.coroutines.flow.Flow

/**
 * Represents an event source that can trigger a reconfiguration.
 */
interface ReconfigurationEvent<T : Any> : Initializable {
    /**
     * Represents the flow of events that must be monitored for triggering the reconfiguration.
     */
    fun eventsFlow(): Flow<T>

    /**
     * Represents the condition that must be satisfied for triggering the reconfiguration.
     * This method is called for each [event] received by the [eventsFlow].
     */
    fun condition(event: T): Boolean

    /**
     * Represents the flow of results of the reconfiguration.
     * It is a companion flow of [eventsFlow].
     */
    fun resultsFlow(): Flow<ReconfigurationResult<T>>

    /**
     * Method that must be called on each evaluated event.
     * For each event, a [result] must be provided.
     */
    suspend fun onProcessedEvent(result: ReconfigurationResult<T>)
}
