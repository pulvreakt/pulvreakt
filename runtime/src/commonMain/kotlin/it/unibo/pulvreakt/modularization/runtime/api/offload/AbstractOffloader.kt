package it.unibo.pulvreakt.modularization.runtime.api.offload

import it.unibo.pulvreakt.modularization.injection.Injection
import it.unibo.pulvreakt.modularization.runtime.api.network.Network
import org.kodein.di.DI
import org.kodein.di.instance

abstract class AbstractOffloader<ID : Any> : Offloader<ID> {
    override val di: DI by lazy {
        Injection.getModule().getOrNull() ?: error(
            """
            Tried to access to the Dependency Injection module before it is initialized.
            Please, initialize the Injection object via the setupModule(DI) method.
            """.trimIndent(),
        )
    }

    protected val network: Network by instance()
}
