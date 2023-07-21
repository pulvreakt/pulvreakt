package it.unibo.pulvreakt.runtime.errors

import it.unibo.pulvreakt.runtime.unit.errors.UnitManagerError

sealed interface RuntimeError

data class DeviceConfigurationNotFound(val device: String) : RuntimeError

data class WrapUnitManagerError(val error: UnitManagerError) : RuntimeError

object UnitManagerNotInitialized : RuntimeError
