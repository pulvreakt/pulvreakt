package it.unibo.pulvreakt.modularization.dsl.data

import it.unibo.pulvreakt.modularization.api.module.Module

typealias ModularizedSystem = Map<Module<*, *, *>, Set<Module<*, *, *>>>
