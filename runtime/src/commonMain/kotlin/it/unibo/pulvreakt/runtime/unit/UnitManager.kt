package it.unibo.pulvreakt.runtime.unit

import arrow.core.Either
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.api.initializable.InjectAwareResource
import it.unibo.pulvreakt.api.initializable.ManagedResource
import it.unibo.pulvreakt.dsl.model.DeviceSpecification
import it.unibo.pulvreakt.runtime.RuntimeContext
import it.unibo.pulvreakt.runtime.unit.errors.UnitManagerError

/**
 * Represents the manager of the deployment unit.
 */
internal interface UnitManager : ManagedResource<UnitManagerError> {

    fun setupContext(context: RuntimeContext<*>)

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
        operator fun invoke(deviceSpecification: DeviceSpecification<*>): UnitManager = UnitManagerImpl(deviceSpecification)
    }
}
