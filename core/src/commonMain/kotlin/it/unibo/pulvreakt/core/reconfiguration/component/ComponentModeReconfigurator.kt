package it.unibo.pulvreakt.core.reconfiguration.component

import it.unibo.pulvreakt.core.communicator.Mode
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.component.ComponentRef
import kotlinx.coroutines.flow.Flow

/**
 * Represents the ability of a [Component] to reconfigure its mode.
 */
interface ComponentModeReconfigurator {

    /**
     * Returns the new [Mode] for the given [Component].
     */
    fun receiveModeUpdates(): Flow<Pair<ComponentRef, Mode>>
}
