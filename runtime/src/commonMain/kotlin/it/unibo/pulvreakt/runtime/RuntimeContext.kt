package it.unibo.pulvreakt.runtime

import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.api.communication.LocalChannelManager
import it.unibo.pulvreakt.api.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.runtime.component.ComponentManager

internal interface RuntimeContext<ID : Any> {
    val context: Context<ID>
    val localChannelManager: LocalChannelManager
    val componentManager: ComponentManager
    val componentModeReconfigurator: ComponentModeReconfigurator
        get() = ComponentModeReconfigurator()
}
