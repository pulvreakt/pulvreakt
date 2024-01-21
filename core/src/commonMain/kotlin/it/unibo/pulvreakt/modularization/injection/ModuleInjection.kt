package it.unibo.pulvreakt.modularization.injection

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import org.kodein.di.DI

sealed interface InjectionError
data object InjectionNotInitializedError : InjectionError
data object InjectionAlreadyInitializedError : InjectionError

object Injection {
    private lateinit var di: DI

    fun setupModule(diModule: DI): Either<InjectionAlreadyInitializedError, Unit> = either {
        ensure(::di.isInitialized) { InjectionAlreadyInitializedError }
        di = diModule
    }

    fun getModule(): Either<InjectionNotInitializedError, DI> = either {
        ensure(::di.isInitialized) { InjectionNotInitializedError }
        di
    }
}
