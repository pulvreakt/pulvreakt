package it.unibo.pulvreakt.runtime.unit.errors

import it.unibo.pulvreakt.core.component.errors.ComponentError
import it.unibo.pulvreakt.runtime.component.errors.ComponentManagerError

sealed interface UnitManagerError

data class WrapComponentError(val error: ComponentError) : UnitManagerError

data class WrapComponentManagerError(val error: ComponentManagerError) : UnitManagerError
