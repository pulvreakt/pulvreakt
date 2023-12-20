package it.unibo.pulvreakt.api.reconfiguration.component

import it.unibo.pulvreakt.api.communication.Mode
import it.unibo.pulvreakt.api.component.Component
import it.unibo.pulvreakt.api.component.ComponentRef
import kotlinx.coroutines.flow.Flow

/**
 * Represents the ability of a [Component] to reconfigure its mode.
 */
interface ComponentModeReconfigurator {

    /**
     * Returns the new [Mode] for the given [Component].
     */
    fun receiveModeUpdates(): Flow<Pair<ComponentRef, Mode>>

    /**
     * Sets the new [Mode] for the given [Component].
     */
    suspend fun setMode(component: ComponentRef, mode: Mode)

    companion object {
        /**
         * Smart constructor for [ComponentModeReconfigurator].
         */
        operator fun invoke(): ComponentModeReconfigurator = ComponentModeReconfiguratorImpl()
    }
}
