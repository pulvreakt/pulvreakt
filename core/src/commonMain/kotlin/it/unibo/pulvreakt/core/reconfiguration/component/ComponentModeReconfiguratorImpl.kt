package it.unibo.pulvreakt.core.reconfiguration.component

import it.unibo.pulvreakt.core.communicator.Mode
import it.unibo.pulvreakt.core.component.ComponentRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class ComponentModeReconfiguratorImpl : ComponentModeReconfigurator {
    override fun receiveModeUpdates(): Flow<Pair<ComponentRef, Mode>> = emptyFlow()
    override suspend fun setMode(component: ComponentRef, mode: Mode) = Unit
}
