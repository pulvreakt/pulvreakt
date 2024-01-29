package it.unibo.pulvreakt.modularization.runtime.api.offload

import arrow.core.Either
import arrow.resilience.Schedule
import it.unibo.pulvreakt.modularization.api.module.SymbolicModule
import it.unibo.pulvreakt.modularization.api.utils.ManagedResource
import it.unibo.pulvreakt.modularization.runtime.errors.OffloadError
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DIAware

sealed interface Operation

data object Start : Operation

data object Stop : Operation

typealias OffloadResult<Result> = Either<OffloadError, Result>

/**
 * Manage the offloading process the modules.
 */
interface Offloader<ID : Any> : ManagedResource<Nothing>, DIAware {
    /**
     * Specifies the strategy to use to offload the modules.
     * Returns a [Schedule] taking a [Set] of [Pair] of [SymbolicModule] and [ID] and returning a [OffloadResult].
     */
    val reconfigurationStrategy: Schedule<Set<Pair<SymbolicModule, ID>>, OffloadResult<Unit>>

    /**
     * Applies the [reconfigurationStrategy] to the given [toOffload] set.
     */
    suspend fun offload(toOffload: Set<Pair<SymbolicModule, ID>>): OffloadResult<Unit> =
        reconfigurationStrategy.repeat { toOffload }

    /**
     * Returns the modules offloaded as asynchronous [Flow].
     */
    fun modulesOffloadFlow(): Flow<Pair<SymbolicModule, ID>>
}
