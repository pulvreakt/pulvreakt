package it.nicolasfarabegoli.pulverization.dsl.v2

import it.nicolasfarabegoli.pulverization.dsl.v2.model.SystemSpecification

/**
 * Entrypoint of the configuration DSL for the pulverized system.
 */
fun pulverizationSystem(setup: PulverizationSystemScope.() -> Unit): SystemSpecification {
    return PulverizationSystemScope().apply(setup).generate()
}
