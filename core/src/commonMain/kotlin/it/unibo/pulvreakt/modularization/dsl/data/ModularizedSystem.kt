package it.unibo.pulvreakt.modularization.dsl.data

import it.unibo.pulvreakt.modularization.api.Module

typealias ModularizedSystem = Map<Module<*, *, *>, Set<Module<*, *, *>>>
