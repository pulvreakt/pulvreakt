package it.unibo.pulvreakt.modularization.runtime

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.modularization.api.Capabilities
import it.unibo.pulvreakt.modularization.api.Host
import it.unibo.pulvreakt.modularization.dsl.data.ModularizedSystem
import it.unibo.pulvreakt.modularization.injection.Injection
import it.unibo.pulvreakt.modularization.runtime.api.ModularizationRuntime
import it.unibo.pulvreakt.modularization.runtime.api.RuntimeResult
import it.unibo.pulvreakt.modularization.runtime.api.bootstrap.Bootstrapper
import it.unibo.pulvreakt.modularization.runtime.api.network.Network
import it.unibo.pulvreakt.modularization.runtime.api.scheduler.Scheduler
import it.unibo.pulvreakt.modularization.runtime.errors.RuntimeBootstrapError
import it.unibo.pulvreakt.modularization.runtime.errors.RuntimeError
import it.unibo.pulvreakt.modularization.runtime.errors.RuntimeNetworkError
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

internal class ModularizationRuntimeImpl<C : Capabilities>(
    override val systemConfiguration: ModularizedSystem,
    override val runningHost: Host<C>,
    override val network: Network,
    override val scheduler: Scheduler,
    override val bootstrapper: Bootstrapper,
) : ModularizationRuntime<C> {
    override suspend fun start(): RuntimeResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun stop(): RuntimeResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun setup(): Either<RuntimeError, Unit> =
        either {
            initializeInjector()
            network.setup().mapLeft { RuntimeNetworkError(it) }.bind()
            scheduler.setup().bind()
            bootstrapper.setup().mapLeft { RuntimeBootstrapError(it) }.bind()
        }

    override suspend fun teardown(): Either<RuntimeError, Unit> =
        either {
            network.teardown().mapLeft { RuntimeNetworkError(it) }.bind()
            scheduler.teardown().bind()
            bootstrapper.setup().mapLeft { RuntimeBootstrapError(it) }.bind()
        }

    private fun initializeInjector() {
        val module = DI {
            bind<Host<C>> { singleton { runningHost } }
            bind<Network> { singleton { network } }
        }
        Injection.setupModule(module)
    }
}
