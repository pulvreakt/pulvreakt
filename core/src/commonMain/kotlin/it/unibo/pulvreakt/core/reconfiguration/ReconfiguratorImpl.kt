package it.unibo.pulvreakt.core.reconfiguration

import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.kodein.di.DI

internal class ReconfiguratorImpl : Reconfigurator {
    override lateinit var di: DI
    // private val protocol by instance<Protocol>()

    override suspend fun reconfigure(newConfiguration: ReconfigurationMessage) = Unit // TODO implement

    override fun receiveConfiguration(): Flow<ReconfigurationMessage> = emptyFlow() // TODO implement

    override suspend fun initialize(): Either<Nothing, Unit> = Unit.right()

    override suspend fun finalize(): Either<Nothing, Unit> = Unit.right()

    override fun setupInjector(kodein: DI) {
        di = kodein
    }
}
