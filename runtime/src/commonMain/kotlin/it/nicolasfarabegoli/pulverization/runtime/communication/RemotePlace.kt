package it.nicolasfarabegoli.pulverization.runtime.communication

import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType

/**
 * Represent a remote place where a component is located.
 * This class hold [who] is the component and [where] is located.
 */
data class RemotePlace(val who: String, val where: String)

/**
 * TODO.
 */
interface RemotePlaceProvider {
    /**
     * TODO.
     */
    operator fun get(type: PulverizedComponentType): RemotePlace?
}
