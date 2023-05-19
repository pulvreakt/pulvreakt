package it.unibo.pulvreakt.dsl

import it.unibo.pulvreakt.dsl.model.SystemSpecification

/**
 * Entrypoint of the configuration DSL for the pulverized system.
 */
fun pulverizationSystem(setup: PulverizationSystemScope.() -> Unit): SystemSpecification {
    return PulverizationSystemScope().apply(setup).generate()
}
