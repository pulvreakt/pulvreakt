package it.unibo.pulvreakt.modularization.runtime.api.bootstrap

import arrow.core.Either
import it.unibo.pulvreakt.modularization.api.module.SymbolicModule
import it.unibo.pulvreakt.modularization.api.utils.ManagedResource
import it.unibo.pulvreakt.modularization.runtime.errors.BootstrapperError

typealias BootstrapperResult = Either<BootstrapperError, Unit>

interface Bootstrapper : ManagedResource<BootstrapperError> {
    suspend fun bootstrap(modules: Set<SymbolicModule>): BootstrapperResult
}
