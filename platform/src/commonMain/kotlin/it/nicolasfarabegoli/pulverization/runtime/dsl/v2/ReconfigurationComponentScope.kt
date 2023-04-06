package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host

class ReconfigurationComponentScope {
    infix fun ComponentType.movesTo(to: Host) { }
}
