package it.nicolasfarabegoli.pulverization.component

import it.nicolasfarabegoli.pulverization.communication.BidirectionalCommunicator
import it.nicolasfarabegoli.pulverization.communication.ReceiverCommunicator
import it.nicolasfarabegoli.pulverization.communication.SenderCommunicator

/**
 * Models the concept of _Component_ belonging to a _Device_.
 * Contains a [id] and a [cycle] method to execute the logic of the component.
 */
interface DeviceComponent<I> {
    /**
     * The [id] of the device.
     */
    val id: I

    /**
     * Execute a single cycle of the device logic.
     */
    suspend fun cycle()

    /**
     * Used to release the resources used by the [DeviceComponent].
     * By default no operations are executed.
     */
    fun finalize() {}
}

/**
 * Models a [DeviceComponent] with the capability of receiving only messages from other components.
 * This type of component is _passive_.
 */
interface ReceiveOnlyDeviceComponent<out Receive, I> : ReceiverCommunicator<Receive, I>, DeviceComponent<I>

/**
 * Models a [DeviceComponent] with the capability of only sending messages to other components.
 * This type of component is _proactive_.
 */
interface SendOnlyDeviceComponent<in Send, I> : SenderCommunicator<Send, I>, DeviceComponent<I>

/**
 * Models a [DeviceComponent] with the capability of both sending and receiving messages from other components.
 */
interface SendReceiveDeviceComponent<in Send, out Receive, I> :
    BidirectionalCommunicator<Send, Receive, I>, DeviceComponent<I>
