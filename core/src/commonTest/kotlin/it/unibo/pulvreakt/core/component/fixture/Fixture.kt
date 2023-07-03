package it.unibo.pulvreakt.core.component.fixture

import it.unibo.pulvreakt.core.communicator.Mode
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.reconfiguration.component.ComponentModeReconfigurator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class TestComponentModeReconfigurator : ComponentModeReconfigurator {
    override fun receiveModeUpdates(): Flow<Pair<ComponentRef<*>, Mode>> = emptyFlow()
}
