package it.unibo.pulvreakt.core.communicator

import it.unibo.pulvreakt.core.component.ComponentRef
import kotlinx.coroutines.flow.MutableSharedFlow

internal class LocalCommunicatorManager {
    private var sharedFlowRegistry:
        Map<Pair<ComponentRef, ComponentRef>, Pair<MutableSharedFlow<ByteArray>, MutableSharedFlow<ByteArray>>> = emptyMap()

    fun getLocalCommunicator(sourceComponent: ComponentRef, destinationComponent: ComponentRef): Communicator {
        val key = sourceComponent to destinationComponent
        val reverseKey = destinationComponent to sourceComponent
        val (inFlow, outFlow) = sharedFlowRegistry[key] ?: run {
            val (inF, outF) = MutableSharedFlow<ByteArray>(1) to MutableSharedFlow<ByteArray>(1)
            sharedFlowRegistry += key to (inF to outF)
            sharedFlowRegistry += reverseKey to (outF to inF)
            inF to outF // Return the newly created
        }
        return LocalCommunicator(inFlow, outFlow)
    }
}
