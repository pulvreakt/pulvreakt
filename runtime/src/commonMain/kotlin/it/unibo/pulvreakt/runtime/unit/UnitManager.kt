package it.unibo.pulvreakt.runtime.unit

import arrow.core.Either
import it.unibo.pulvreakt.core.utils.Initializable
import it.unibo.pulvreakt.core.utils.PulvreaktInjected
import it.unibo.pulvreakt.dsl.model.DeviceSpecification
import it.unibo.pulvreakt.runtime.unit.errors.UnitManagerError

interface UnitManager : Initializable<UnitManagerError>, PulvreaktInjected {
    suspend fun start(): Either<UnitManagerError, Unit>
    suspend fun stop(): Either<UnitManagerError, Unit>

    companion object {
        operator fun invoke(deviceSpecification: DeviceSpecification): UnitManager = UnitManagerImpl(deviceSpecification)
    }
}
