package it.unibo.pulvreakt.modularization.dsl.data

import it.unibo.pulvreakt.modularization.api.module.SymbolicModule

typealias ModularizedSystem = Map<SymbolicModule, Set<SymbolicModule>>
