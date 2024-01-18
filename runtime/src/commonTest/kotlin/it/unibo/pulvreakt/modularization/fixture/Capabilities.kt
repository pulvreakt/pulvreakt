package it.unibo.pulvreakt.modularization.fixture

import it.unibo.pulvreakt.modularization.api.Capabilities

data object TemperatureSensor : Capabilities

sealed interface UnionCapabilities : Capabilities {
    data object A : UnionCapabilities
    data object B : UnionCapabilities
}
