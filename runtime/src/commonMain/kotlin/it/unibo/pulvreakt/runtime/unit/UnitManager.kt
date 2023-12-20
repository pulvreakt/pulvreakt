package it.unibo.pulvreakt.runtime.unit

import arrow.core.Either
import it.unibo.pulvreakt.api.initializable.InjectAwareResource
import it.unibo.pulvreakt.api.initializable.ManagedResource
import it.unibo.pulvreakt.dsl.model.DeviceSpecification
import it.unibo.pulvreakt.runtime.unit.errors.UnitManagerError

/**
 * Represents the manager of the deployment unit.
 */
interface UnitManager : ManagedResource<UnitManagerError>, InjectAwareResource {
    /**
     * Starts the deployment unit.
     */
    suspend fun start(): Either<UnitManagerError, Unit>

    /**
     * Stops the deployment unit.
     */
    suspend fun stop(): Either<UnitManagerError, Unit>

    companion object {
        /**
         * Smart constructor for [UnitManager].
         */
        operator fun invoke(deviceSpecification: DeviceSpecification): UnitManager =
            UnitManagerImpl(deviceSpecification)
    }
}
