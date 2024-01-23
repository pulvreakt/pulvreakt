package it.unibo.pulvreakt.modularization.runtime.api.offload

import arrow.core.Either
import it.unibo.pulvreakt.modularization.api.module.SymbolicModule
import it.unibo.pulvreakt.modularization.api.utils.ManagedResource
import it.unibo.pulvreakt.modularization.runtime.api.network.Network
import it.unibo.pulvreakt.modularization.runtime.errors.OffloadError
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DIAware

sealed interface Operation

data object Start : Operation

data object Stop : Operation

typealias OffloadResult<Result> = Either<OffloadError, Result>

/**
 * Manage the offloading process the modules leveraging the [network].
 */
interface Offloader<ID : Any> : ManagedResource<Nothing>, DIAware {
    val failureTimeout: Long

    suspend fun offloadStrategy(
        toOffload: Set<Pair<SymbolicModule, ID>>,
        network: Network,
    )

    suspend fun offload(toOffload: Set<Pair<SymbolicModule, ID>>): OffloadResult<Unit>

    fun modulesOffloadFlow(): Flow<Pair<SymbolicModule, ID>>
}
