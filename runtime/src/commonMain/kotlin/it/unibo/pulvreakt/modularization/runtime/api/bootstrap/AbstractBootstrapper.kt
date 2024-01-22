package it.unibo.pulvreakt.modularization.runtime.api.bootstrap

import arrow.core.Either
import it.unibo.pulvreakt.modularization.injection.Injection
import it.unibo.pulvreakt.modularization.runtime.api.network.Network
import it.unibo.pulvreakt.modularization.runtime.errors.BootstrapperError
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

/**
 * Abstract implementation of a [Bootstrapper] module that provides a [network] instance.
 */
abstract class AbstractBootstrapper : Bootstrapper, DIAware {
    override val di: DI by lazy {
        Injection.getModule().getOrNull() ?: error(
            """
            Tried to access to the Dependency Injection module before it is initialized.
            Please, initialize the Injection object via the setupModule(DI) method.
            """.trimIndent(),
        )
    }
    protected val network: Network by instance()

    override suspend fun setup(): Either<BootstrapperError, Unit> {
        TODO("Not yet implemented")
    }
}
