package it.unibo.pulvreakt.dsl.model.old

import it.unibo.pulvreakt.core.reconfiguration.event.ReconfigurationEvent

class DeviceReconfigurationRule(
    val reconfigurationEvent: ReconfigurationEvent<*>,
    val newConfgiuration: NewConfiguration,
)
