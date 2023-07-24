package it.unibo.pulvreakt.runtime.reconfigurator

import arrow.core.Either
import arrow.core.right
import it.unibo.pulvreakt.runtime.reconfigurator.errors.UnitReconfiguratorError
import org.kodein.di.DI

internal class UnitReconfiguratorImpl : UnitReconfigurator {
    override lateinit var di: DI

    override suspend fun initialize(): Either<UnitReconfiguratorError, Unit> = Unit.right()

    override suspend fun finalize(): Either<UnitReconfiguratorError, Unit> = Unit.right()

    override fun setupInjector(kodein: DI) {
        di = kodein
    }
}
