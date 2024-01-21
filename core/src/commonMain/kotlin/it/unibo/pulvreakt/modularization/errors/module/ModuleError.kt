package it.unibo.pulvreakt.modularization.errors.module

import it.unibo.pulvreakt.modularization.api.module.SymbolicModule

sealed interface ModuleError
data class ModuleNotRegistered(val module: SymbolicModule) : ModuleError
data object ReceiveOperationNotSupported : ModuleError
