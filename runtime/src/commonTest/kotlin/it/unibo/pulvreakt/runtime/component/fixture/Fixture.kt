package it.unibo.pulvreakt.runtime.component.fixture

import it.unibo.pulvreakt.api.communication.Mode
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.reconfiguration.component.ComponentModeReconfigurator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class TestComponentModeReconfigurator : ComponentModeReconfigurator {
    override fun receiveModeUpdates(): Flow<Pair<ComponentRef, Mode>> = emptyFlow()
    override suspend fun setMode(component: ComponentRef, mode: Mode) = Unit
}
