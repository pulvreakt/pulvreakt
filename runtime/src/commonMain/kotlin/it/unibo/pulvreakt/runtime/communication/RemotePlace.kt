package it.unibo.pulvreakt.runtime.communication

import it.unibo.pulvreakt.dsl.model.ComponentType
import it.unibo.pulvreakt.runtime.context.ExecutionContext

/**
 * Represent a remote place where a component is located.
 * This class hold [who] is the component and [where] is located.
 */
data class RemotePlace(val who: String, val where: String)

/**
 * Provider of [RemotePlace] using a [context].
 */
interface RemotePlaceProvider {
    /**
     * The execution context.
     */
    val context: ExecutionContext

    /**
     * Return a [RemotePlace] (if any) of a given [type].
     */
    operator fun get(type: ComponentType): RemotePlace?
}
