package it.unibo.pulvreakt.modularization.runtime.api

import arrow.core.Either
import it.unibo.pulvreakt.modularization.api.Capabilities
import it.unibo.pulvreakt.modularization.api.Host
import it.unibo.pulvreakt.modularization.api.module.Module
import it.unibo.pulvreakt.modularization.api.utils.ManagedResource
import it.unibo.pulvreakt.modularization.dsl.data.ModularizedSystem
import it.unibo.pulvreakt.modularization.runtime.api.bootstrap.Bootstrapper
import it.unibo.pulvreakt.modularization.runtime.api.network.Network
import it.unibo.pulvreakt.modularization.runtime.api.scheduler.Scheduler
import it.unibo.pulvreakt.modularization.runtime.errors.RuntimeError

typealias RuntimeResult<Result> = Either<RuntimeError, Result>

/**
 * Models the runtime of the modularization middleware.
 * The runtime operates over a [systemConfiguration], using a [network] for the communication and a [scheduler] to execute the modules.
 * The middleware uses the [bootstrapper] to determine the initial distribution of the modules over the infrastructure.
 * The [runningHost] represent the actual host where the middleware is running on.
 */
interface ModularizationRuntime<ID : Any, C : Capabilities> : ManagedResource<RuntimeError> {
    val runningHost: Host<C>
    val bootstrapper: Bootstrapper<ID>
    val systemConfiguration: ModularizedSystem
    val network: Network
    val scheduler: Scheduler<ID>

    /**
     * Starts the execution of the middleware.
     */
    suspend fun start(): RuntimeResult<Unit>

    /**
     * Starts the execution of the given [modules].
     * May fails with a [RuntimeError].
     */
    suspend fun startModules(modules: Set<Pair<Module<*, *, *>, ID>>): RuntimeResult<Unit>

    /**
     * Stop the execution of the middleware.
     * May fails with a [RuntimeError].
     */
    suspend fun stop(): RuntimeResult<Unit>
}
