package it.unibo.pulvreakt.core.utils

import org.kodein.di.DI
import org.kodein.di.DIAware

interface PulvreaktInjected : DIAware {
    fun setupInjector(kodein: DI)
}
