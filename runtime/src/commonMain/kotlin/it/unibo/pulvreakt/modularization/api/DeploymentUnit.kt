package it.unibo.pulvreakt.modularization.api

import arrow.core.Either
import it.unibo.pulvreakt.modularization.dsl.data.ModularizedSystem
import it.unibo.pulvreakt.modularization.errors.DeploymentUnitError
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.singleton

typealias Outcome = Either<DeploymentUnitError, Unit>

sealed interface DeploymentUnit<C : Capabilities> : DIAware {
    val runningHost: Host<C>
    val scheduler: Scheduler

    fun <Input : Any, Output : Any> registerModule(
        module: Module<*, Input, Output>,
        inputKSerializer: KSerializer<Input>,
        outputKSerializer: KSerializer<Output>,
    )

    /**
     * Setup the deployment unit.
     */
    suspend fun setup(): Outcome

    /**
     * Teardown the deployment unit.
     */
    suspend fun teardown(): Outcome

    /**
     * Starts the deployment unit with the registered modules via [registerModule].
     * This method verifies if the registered modules can be deployed on this [runningHost] or on a neighbour host.
     * If the [runningHost] do not provide the [Capabilities] required by a module, its execution will be delegated to a neighbour host.
     * If no neighbour host can execute the module, it will be paused until a valid neighbour host is available.
     * The execution of the modules is managed by the [scheduler].
     */
    suspend fun start(): Outcome

    /**
     * Stop the deployment unit.
     */
    suspend fun stop(): Outcome

    companion object {
        inline fun <C : Capabilities, reified Input : Any, reified Output : Any> DeploymentUnit<C>.registerModule(module: Module<*, Input, Output>) =
            registerModule(module, serializer(), serializer())
    }
}

inline fun <reified C : Capabilities> deploymentUnit(
    configuration: ModularizedSystem,
    host: Host<C>,
    scheduler: Scheduler,
    config: DeploymentUnit<C>.() -> Unit,
): DeploymentUnit<C> {
    val diModule = DI {
        bind<C> { singleton { host.exposedCapabilities } }
    }
    return DeploymentUnitImpl(diModule, host, scheduler, configuration).apply(config)
}
