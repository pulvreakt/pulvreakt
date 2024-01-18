package it.unibo.pulvreakt.modularization.fixture

import it.unibo.pulvreakt.modularization.api.Host

class EmbeddedDeviceHost : Host<TemperatureSensor> {
    override val exposedCapabilities: TemperatureSensor = TemperatureSensor

    override fun <V> set(key: String, value: V) {}
    override fun <V> get(key: String): V? = null
    override fun <V> getOrElse(key: String, defaultValue: () -> V): V = defaultValue()
}

class HostA : Host<UnionCapabilities> {
    override val exposedCapabilities: UnionCapabilities = UnionCapabilities.A

    override fun <V> set(key: String, value: V) {}
    override fun <V> get(key: String): V? = null
    override fun <V> getOrElse(key: String, defaultValue: () -> V): V = defaultValue()
}

