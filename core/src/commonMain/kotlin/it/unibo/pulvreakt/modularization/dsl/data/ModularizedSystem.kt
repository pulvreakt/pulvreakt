package it.unibo.pulvreakt.modularization.dsl.data

import it.unibo.pulvreakt.modularization.api.module.Module
import it.unibo.pulvreakt.modularization.api.module.SymbolicModule

data class ModularizedSystem(val modules: Set<Module<*, *, *>>, val configuration: Map<SymbolicModule, Set<SymbolicModule>>)
