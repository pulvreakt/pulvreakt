package it.unibo.pulvreakt.modularization.runtime.errors

import it.unibo.pulvreakt.modularization.api.module.SymbolicModule

interface OffloadError

data class ModuleOffloadingError(val module: SymbolicModule, val id: Int) : OffloadError, Throwable()
