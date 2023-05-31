package it.unibo.pulvreakt.core.utils

import org.kodein.di.DI
import org.kodein.di.DIAware

/**
 * Represents the ability of a class to DI aware.
 */
interface PulvreaktInjected : DIAware {
    /**
     * Sets up the injector.
     */
    fun setupInjector(kodein: DI)
}
