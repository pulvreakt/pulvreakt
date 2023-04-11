package it.nicolasfarabegoli.pulverization.runtime.communication

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType

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
    val context: Context

    /**
     * Return a [RemotePlace] (if any) of a given [type].
     */
    operator fun get(type: ComponentType): RemotePlace?
}
