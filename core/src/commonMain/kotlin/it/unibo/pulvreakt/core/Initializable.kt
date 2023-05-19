package it.unibo.pulvreakt.core

/**
 * Pulverized component initialization interface.
 */
interface Initializable {
    /**
     * Use this method for setup the component.
     * By default, this method do nothing.
     */
    suspend fun initialize() {}

    /**
     * This method is used to release resources or make any other action when the component is no longer needed.
     * By default, this method do nothing.
     */
    suspend fun finalize() {}
}
