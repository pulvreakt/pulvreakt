package it.nicolasfarabegoli.pulverization.component

import org.koin.core.component.KoinComponent

/**
 * Models the concept of _Component_ belonging to a _Device_.
 * Contains a [id] and a [cycle] method to execute the logic of the component.
 */
interface DeviceComponent<C : Context> : KoinComponent {
    /**
     * The [context] in which the device run.
     */
    val context: C

    /**
     * Initialize the component.
     */
    suspend fun initialize() {}

    /**
     * Execute a single cycle of the device logic.
     */
    suspend fun cycle()

    /**
     * Used to release the resources used by the [DeviceComponent].
     * By default no operations are executed.
     */
    suspend fun finalize() {}
}

// /**
// * Models a [DeviceComponent] with the capability of receiving only messages from other components.
// * This type of component is _passive_.
// */
// interface ReceiveOnlyDeviceComponent<out Receive, I : DeviceID> :
// ReceiverCommunicator<Receive, I>, DeviceComponent<I>
//
// /**
// * Models a [DeviceComponent] with the capability of only sending messages to other components.
// * This type of component is _proactive_.
// */
// interface SendOnlyDeviceComponent<in Send, I : DeviceID> : SenderCommunicator<Send, I>, DeviceComponent<I>
//
// /**
// * Models a [DeviceComponent] with the capability of both sending and receiving messages from other components.
// */
// interface SendReceiveDeviceComponent<in Send, out Receive, I : DeviceID> :
//    BidirectionalCommunicator<Send, Receive, I>, DeviceComponent<I>
