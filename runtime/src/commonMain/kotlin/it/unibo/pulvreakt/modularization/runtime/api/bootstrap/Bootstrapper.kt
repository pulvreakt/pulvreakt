package it.unibo.pulvreakt.modularization.runtime.api.bootstrap

import arrow.core.Either
import it.unibo.pulvreakt.modularization.api.module.SymbolicModule
import it.unibo.pulvreakt.modularization.api.utils.ManagedResource
import it.unibo.pulvreakt.modularization.runtime.errors.BootstrapperError

typealias BootstrapperResult<Result> = Either<BootstrapperError, Result>

interface Bootstrapper<ID : Any> : ManagedResource<BootstrapperError> {
    /**
     * Returns the set of modules that must be executed locally.
     * Can fail with a [BootstrapperError] in case no suitable allocation is found for the modules.
     */
    suspend fun bootstrap(modules: Set<Pair<SymbolicModule, ID>>): BootstrapperResult<Set<Pair<SymbolicModule, ID>>>
}
