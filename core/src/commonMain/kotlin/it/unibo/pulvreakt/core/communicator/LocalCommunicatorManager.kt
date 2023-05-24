package it.unibo.pulvreakt.core.communicator

import kotlinx.coroutines.flow.MutableSharedFlow

internal class LocalCommunicatorManager {
    private var sharedFlowRegistry: Map<Set<String>, MutableSharedFlow<ByteArray>> = emptyMap()

    fun getLocalCommunicator(sourceComponent: String, destinationComponent: String): Communicator {
        val key = setOf(sourceComponent, destinationComponent)
        val sharedFlow = sharedFlowRegistry[key] ?: MutableSharedFlow()
        sharedFlowRegistry += key to sharedFlow
        return LocalCommunicator(sharedFlow)
    }
}
