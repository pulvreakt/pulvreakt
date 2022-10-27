package it.nicolasfarabegoli.pulverization.component

import it.nicolasfarabegoli.pulverization.core.DeviceID

/**
 * Hold information about the context in which the [DeviceComponent] run.
 */
interface Context {
    /**
     * The identification of the device.
     * This [id] may be used by the underlying layers.
     */
    val id: DeviceID
}
