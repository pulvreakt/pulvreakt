package it.unibo.pulvreakt.component

/**
 * The context on which a component run on.
 */
interface Context {
    /**
     * The ID of the device.
     */
    val deviceID: String
}
