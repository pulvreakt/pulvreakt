package it.unibo.pulvreakt.api.communication

import it.unibo.pulvreakt.api.component.ComponentRef
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Manages the creation of [LocalChannel]s.
 */
class LocalChannelManager {
    private var sharedFlowRegistry:
        Map<Pair<ComponentRef, ComponentRef>, Pair<MutableSharedFlow<ByteArray>, MutableSharedFlow<ByteArray>>> = emptyMap()

    /**
     * Returns a [LocalChannel] that can be used to communicate between the given components.
     */
    fun getLocalCommunicator(sourceComponent: ComponentRef, destinationComponent: ComponentRef): Channel {
        val key = sourceComponent to destinationComponent
        val reverseKey = destinationComponent to sourceComponent
        val (inFlow, outFlow) = sharedFlowRegistry[key] ?: run {
            val (inF, outF) = MutableSharedFlow<ByteArray>(1) to MutableSharedFlow<ByteArray>(1)
            sharedFlowRegistry += key to (inF to outF)
            sharedFlowRegistry += reverseKey to (outF to inF)
            inF to outF // Return the newly created
        }
        return LocalChannel(inFlow, outFlow)
    }
}
