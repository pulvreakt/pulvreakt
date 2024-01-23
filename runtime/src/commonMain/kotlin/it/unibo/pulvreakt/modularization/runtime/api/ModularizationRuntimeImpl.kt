package it.unibo.pulvreakt.modularization.runtime.api

import arrow.core.Either
import arrow.core.raise.either
import arrow.fx.coroutines.parMap
import it.unibo.pulvreakt.modularization.api.Capabilities
import it.unibo.pulvreakt.modularization.api.Host
import it.unibo.pulvreakt.modularization.api.module.Module
import it.unibo.pulvreakt.modularization.api.module.SymbolicModule
import it.unibo.pulvreakt.modularization.api.module.module
import it.unibo.pulvreakt.modularization.dsl.data.ModularizedSystem
import it.unibo.pulvreakt.modularization.injection.Injection
import it.unibo.pulvreakt.modularization.runtime.api.bootstrap.Bootstrapper
import it.unibo.pulvreakt.modularization.runtime.api.network.Network
import it.unibo.pulvreakt.modularization.runtime.api.scheduler.Scheduler
import it.unibo.pulvreakt.modularization.runtime.errors.RuntimeBootstrapError
import it.unibo.pulvreakt.modularization.runtime.errors.RuntimeError
import it.unibo.pulvreakt.modularization.runtime.errors.RuntimeNetworkError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

internal class ModularizationRuntimeImpl<ID : Any, C : Capabilities>(
    override val systemConfiguration: ModularizedSystem,
    override val runningHost: Host<C>,
    override val network: Network,
    override val scheduler: Scheduler<ID>,
    override val bootstrapper: Bootstrapper<ID>,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ModularizationRuntime<ID, C> {
    private val activeModules: MutableSet<Pair<Module<*, *, *>, ID>> = mutableSetOf()
    private val scope = CoroutineScope(dispatcher + Job())

    override suspend fun start(): RuntimeResult<Unit> =
        either {
            startSchedulerExecution()
            startNetworkProcessing()
        }

    override suspend fun startModules(modules: Set<Pair<Module<*, *, *>, ID>>): RuntimeResult<Unit> =
        either {
            val symbolicModules = modules.map { (mod, id) -> module(mod) to id }.toSet()
            val localSymbolicModules = bootstrapper.bootstrap(symbolicModules).mapLeft { RuntimeBootstrapError(it) }.bind()
            val localModules = modules.filter { (mod, id) -> (module(mod) to id) in localSymbolicModules }.toSet()
            activeModules += localModules
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

    private fun startSchedulerExecution() {
        scope.launch {
            try {
                scheduler.scheduledModulesFlow().collect { modules ->
                    fromSymbolToModule(modules, activeModules).parMap { module ->
                        when (val result = module.execute()) {
                            is Either.Left -> error(result.value)
                            is Either.Right -> { }
                        }
                    }
                }
            } catch (_: CancellationException) {
                // Do nothing
            }
        }
    }

    private fun fromSymbolToModule(
        symModules: Set<Pair<SymbolicModule, ID>>,
        activeModules: Set<Pair<Module<*, *, *>, ID>>,
    ): Set<Module<*, *, *>> =
        symModules.map { (mod, id) ->
            val module = activeModules.firstOrNull { mod == module(it.first) to id }
            requireNotNull(module?.first)
        }.toSet()

    private fun startNetworkProcessing() {
        scope.launch {
            network.messagesFlow().collect {
            }
        }
    }
}
