package it.unibo.pulvreakt.core.component

import arrow.core.Either

interface Initializable {
    suspend fun initialize(): Either<String, Unit>
    suspend fun finalize(): Either<String, Unit>
}
