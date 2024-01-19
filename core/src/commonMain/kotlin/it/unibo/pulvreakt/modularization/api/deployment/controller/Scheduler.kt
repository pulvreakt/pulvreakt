package it.unibo.pulvreakt.modularization.api.deployment.controller

import it.unibo.pulvreakt.modularization.api.SymbolicModule
import kotlinx.coroutines.flow.Flow

interface Scheduler {
    fun schedule(): Flow<SymbolicModule>
}
