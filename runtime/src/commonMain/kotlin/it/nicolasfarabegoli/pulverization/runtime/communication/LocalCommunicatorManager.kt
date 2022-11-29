package it.nicolasfarabegoli.pulverization.runtime.communication

import kotlinx.coroutines.channels.Channel

internal object LocalCommunicatorManager {
    val stateInstance: Channel<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) { Channel() }
    val actuatorsInstance: Channel<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) { Channel() }
    val sensorsInstance: Channel<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) { Channel() }
    val communicationInstance: Channel<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) { Channel() }
    val behaviourStateInstance: Channel<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) { Channel() }
    val behaviourCommunicationInstance: Channel<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) { Channel() }
    val behaviourSensorsInstance: Channel<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) { Channel() }
    val behaviourActuatorsInstance: Channel<ByteArray> by lazy(LazyThreadSafetyMode.PUBLICATION) { Channel() }
}
