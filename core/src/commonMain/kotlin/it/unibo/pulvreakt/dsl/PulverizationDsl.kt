package it.unibo.pulvreakt.dsl

import it.unibo.pulvreakt.dsl.scopes.PulverizationScope

fun pulverization(block: PulverizationScope.() -> Unit) = PulverizationScope().apply { block() }
