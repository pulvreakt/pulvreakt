package it.unibo.pulvreakt.core.utils

import arrow.core.Either

/**
 * Represents the ability of an entity to be initialized and finalized.
 */
interface Initializable {
    /**
     * Initializes the entity.
     */
    suspend fun initialize(): Either<String, Unit>

    /**
     * Finalizes the entity.
     */
    suspend fun finalize(): Either<String, Unit>
}
