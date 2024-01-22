package it.unibo.pulvreakt.modularization.runtime.api.network

import it.unibo.pulvreakt.modularization.api.Host
import it.unibo.pulvreakt.modularization.api.utils.ManagedResource
import it.unibo.pulvreakt.modularization.runtime.api.network.protocol.Heartbeat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow

typealias NeighborsState = Set<Host<*>>

/**
 * The HeartbeatController interface is responsible for managing the heartbeat mechanism in a network.
 * A [Host] will be considered dead if it does not send a heartbeat for a time greater than [retentionTime].
 * A change in the neighborhood is notified through the [neighborsState] flow.
 * The [createHeartbeat] method is used to create a heartbeat to send to the network.
 */
interface HeartbeatController : ManagedResource<Nothing> {
    val retentionTime: Long

    /**
     * A flow that emits the update of the neighborhood.
     */
    fun neighborsState(): StateFlow<NeighborsState>

    /**
     * Sends a heartbeat to the network.
     */
    suspend fun createHeartbeat(): Heartbeat

    /**
     * Process the [heartbeat] received from the network.
     * A [heartbeat] with a newer timestamp overrides the previous one.
     * If the [heartbeat] is from a new [Host], it is added to the neighborhood.
     */
    fun receiveHeartbeat(heartbeat: Heartbeat)

    companion object {
        /**
         * Creates a new [HeartbeatController] with the specified [retentionTime] and optionally a [dispatcher].
         */
        operator fun invoke(
            retentionTime: Long,
            dispatcher: CoroutineDispatcher = Dispatchers.Default,
        ): HeartbeatController = HeartbeatControllerImpl(retentionTime, dispatcher)
    }
}
