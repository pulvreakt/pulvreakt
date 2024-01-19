package it.unibo.pulvreakt.modularization.api

interface Host<C : Capabilities> {
    val exposedCapabilities: C

    operator fun <V> set(key: String, value: V)
    operator fun <V> get(key: String): V?
    fun <V> getOrElse(key: String, defaultValue: () -> V): V
}
