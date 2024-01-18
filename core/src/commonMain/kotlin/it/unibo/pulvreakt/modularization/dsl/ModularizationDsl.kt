package it.unibo.pulvreakt.modularization.dsl

import it.unibo.pulvreakt.modularization.dsl.data.ModularizedSystem

fun modularization(scope: ModularizationScope.() -> Unit): ModularizedSystem = ModularizationScope().apply(scope).build()
