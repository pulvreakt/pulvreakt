package it.unibo.pulvreakt.api.initializable

import org.kodein.di.DI
import org.kodein.di.DIAware

/**
 * Represents a resource that is aware of the dependency injection framework.
 */
interface InjectAwareResource : DIAware {
    /**
     * Configures the module of the dependency injection module that the resource will use.
     */
    fun setupInjector(kodein: DI)
}
