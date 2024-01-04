package it.unibo.pulvreakt.api.initializable

import arrow.core.Either

/**
 * Represent a resource that can be initialized and finalized.
 * The initialization and finalization can either fail with an [Error] or succeed.
 */
interface ManagedResource<out Error> {
    /**
     * Asynchronously initializes the entity.
     * Can [Either] succeed or fail with an [Error].
     */
    suspend fun initialize(): Either<Error, Unit>

    /**
     * Asynchronously finalizes the entity.
     * Can [Either] succeed or fail with an [Error].
     */
    suspend fun finalize(): Either<Error, Unit>
}
