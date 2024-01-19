package it.unibo.pulvreakt.modularization.api.deployment.controller

import it.unibo.pulvreakt.modularization.api.Capabilities
import it.unibo.pulvreakt.modularization.api.Host
import it.unibo.pulvreakt.modularization.api.deployment.network.Network
import it.unibo.pulvreakt.modularization.api.deployment.unit.DeploymentUnit
import it.unibo.pulvreakt.modularization.dsl.data.ModularizedSystem

interface UnitController<C : Capabilities> {
    val configuration: ModularizedSystem
    val host: Host<C>
    val network: Network
    val scheduler: Scheduler
    val deploymentUnit: DeploymentUnit<C>

    suspend fun start()
    suspend fun teardown()
}
