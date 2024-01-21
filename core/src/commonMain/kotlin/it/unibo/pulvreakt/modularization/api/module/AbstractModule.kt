package it.unibo.pulvreakt.modularization.api.module

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import it.unibo.pulvreakt.modularization.api.Capabilities
import it.unibo.pulvreakt.modularization.errors.module.ModuleError
import it.unibo.pulvreakt.modularization.errors.module.ReceiveOperationNotSupported
import it.unibo.pulvreakt.modularization.injection.Injection
import org.kodein.di.DI
import org.kodein.di.DIAware

/**
 * Implementation of [Module] that provides a default implementation of [execute] method.
 */
abstract class AbstractModule<out Cap : Capabilities, Input, Output> : Module<Cap, Input, Output>, DIAware {
    override val di: DI by lazy {
        Injection.getModule().getOrNull() ?: error(
            """
                Tried to access to the Dependency Injection module before it is initialized.
                Please, initialize the Injection object via the setupModule(DI) method
            """.trimIndent(),
        )
    }
    override var inputModules: Set<SymbolicModule> = emptySet()
    override var outputModules: Set<SymbolicModule> = emptySet()

    override suspend fun setup(): Either<ModuleError, Unit> = Unit.right()
    override suspend fun teardown(): Either<ModuleError, Unit> = Unit.right()
    override fun <Type> receive(from: SymbolicModule): ModuleResult<Type> = ReceiveOperationNotSupported.left()

    override fun execute(): ModuleResult<Output> = either {
        val inputs = buildInput().bind()
        invoke(inputs)
    }
}
