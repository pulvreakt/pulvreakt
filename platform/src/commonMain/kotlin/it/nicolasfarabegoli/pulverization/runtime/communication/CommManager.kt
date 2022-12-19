package it.nicolasfarabegoli.pulverization.runtime.communication

import kotlinx.coroutines.flow.MutableSharedFlow

internal class CommManager {
    val stateInstance: MutableSharedFlow<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        MutableSharedFlow(1)
    }
    val actuatorsInstance: MutableSharedFlow<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        MutableSharedFlow(1)
    }
    val sensorsInstance: MutableSharedFlow<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        MutableSharedFlow(1)
    }
    val communicationInstance: MutableSharedFlow<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        MutableSharedFlow(1)
    }
    val behaviourStateInstance: MutableSharedFlow<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        MutableSharedFlow(1)
    }
    val behaviourCommunicationInstance: MutableSharedFlow<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        MutableSharedFlow(1)
    }
    val behaviourSensorsInstance: MutableSharedFlow<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        MutableSharedFlow(1)
    }
    val behaviourActuatorsInstance: MutableSharedFlow<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        MutableSharedFlow(1)
    }
}
