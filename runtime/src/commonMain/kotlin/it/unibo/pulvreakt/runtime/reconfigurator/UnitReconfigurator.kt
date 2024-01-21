package it.unibo.pulvreakt.runtime.reconfigurator

import it.unibo.pulvreakt.api.initializable.ManagedResource
import it.unibo.pulvreakt.runtime.reconfigurator.errors.UnitReconfiguratorError

/**
 * The [UnitReconfigurator] is the main entry point of the reconfigurator.
 */
interface UnitReconfigurator : ManagedResource<UnitReconfiguratorError> {

    companion object {
        /**
         * Smart constructor for [UnitReconfigurator].
         */
        operator fun invoke(): UnitReconfigurator = UnitReconfiguratorImpl()
    }
}
