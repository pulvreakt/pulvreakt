package it.unibo.pulvreakt.core.context

import it.unibo.pulvreakt.core.infrastructure.Host

/**
 * Represents the context of the deployment unit.
 */
interface Context {
    /**
     * The ID of the device.
     */
    val deviceId: Int

    /**
     * The host where the deployment unit is running.
     */
    val host: Host

    companion object {
        /**
         * Smart constructor for [Context].
         */
        operator fun invoke(deviceId: Int, host: Host): Context = object : Context {
            override val deviceId: Int = deviceId
            override val host: Host = host
        }
    }
}
