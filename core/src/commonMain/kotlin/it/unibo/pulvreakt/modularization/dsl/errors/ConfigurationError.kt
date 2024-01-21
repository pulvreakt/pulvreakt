package it.unibo.pulvreakt.modularization.dsl.errors

import it.unibo.pulvreakt.modularization.api.module.SymbolicModule

sealed interface ConfigurationError
data class ModuleNotAvailable(val moduleName: SymbolicModule) : ConfigurationError
