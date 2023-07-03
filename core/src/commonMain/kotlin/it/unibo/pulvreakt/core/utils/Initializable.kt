package it.unibo.pulvreakt.core.utils

import arrow.core.Either

/**
 * Represents the ability of an entity to be initialized and finalized.
 */
interface Initializable<out Error> {
    /**
     * Initializes the entity.
     */
    suspend fun initialize(): Either<Error, Unit>

    /**
     * Finalizes the entity.
     */
    suspend fun finalize(): Either<Error, Unit>
}
