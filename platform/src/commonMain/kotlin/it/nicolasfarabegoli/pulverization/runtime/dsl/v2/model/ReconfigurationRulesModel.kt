package it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model

import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.EventProducer
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.EventReconfiguration

data class OnDeviceReconfigurationRules(
    val rules: Set<EventReconfiguration<*, out EventProducer<*>>>
)
