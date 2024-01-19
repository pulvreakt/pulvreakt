package it.unibo.pulvreakt.modularization.api.deployment.unit

import it.unibo.pulvreakt.modularization.api.Capabilities
import it.unibo.pulvreakt.modularization.api.Module

interface DeploymentUnit<C : Capabilities> {
    suspend fun createAndStartModule(module: Module<*, *, *>, id: Int)
    suspend fun triggerRound(module: Module<*, *, *>, id: Int)
    suspend fun stopModule(module: Module<*, *, *>, id: Int)
}
