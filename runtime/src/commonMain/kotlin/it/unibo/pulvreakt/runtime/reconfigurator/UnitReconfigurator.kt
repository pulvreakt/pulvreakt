package it.unibo.pulvreakt.runtime.reconfigurator

import it.unibo.pulvreakt.core.utils.Initializable
import it.unibo.pulvreakt.core.utils.PulvreaktInjected
import it.unibo.pulvreakt.runtime.reconfigurator.errors.UnitReconfiguratorError

/**
 * The [UnitReconfigurator] is the main entry point of the reconfigurator.
 */
interface UnitReconfigurator : Initializable<UnitReconfiguratorError>, PulvreaktInjected {

    companion object {
        /**
         * Smart constructor for [UnitReconfigurator].
         */
        operator fun invoke(): UnitReconfigurator = UnitReconfiguratorImpl()
    }
}
