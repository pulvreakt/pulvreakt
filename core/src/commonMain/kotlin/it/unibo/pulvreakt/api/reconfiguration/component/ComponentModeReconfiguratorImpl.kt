package it.unibo.pulvreakt.api.reconfiguration.component

import it.unibo.pulvreakt.api.communication.Mode
import it.unibo.pulvreakt.api.component.ComponentRef
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class ComponentModeReconfiguratorImpl : ComponentModeReconfigurator {
    private val _flow = MutableSharedFlow<Pair<ComponentRef, Mode>>(10, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override fun receiveModeUpdates(): Flow<Pair<ComponentRef, Mode>> = _flow.asSharedFlow()
    override suspend fun setMode(component: ComponentRef, mode: Mode) {
        _flow.emit(component to mode)
    }
}
