package it.unibo.pulvreakt.core.reconfiguration.component

import it.unibo.pulvreakt.core.communicator.Mode
import it.unibo.pulvreakt.core.component.Component
import kotlinx.coroutines.flow.Flow

interface ComponentModeReconfigurator {
    fun receiveModeUpdates(): Flow<Pair<Component<*>, Mode>>
}
