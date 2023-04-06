package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.OnDeviceReconfigurationRules
import kotlinx.coroutines.flow.Flow

interface EventProducer<out E : Any> {
    val events: Flow<E>
}

data class EventReconfiguration<P : Any, E : EventProducer<P>>(
    val event: E,
    val predicate: (P) -> Boolean,
)

class OnDeviceScope {
    private var rules: OnDeviceReconfigurationRules = OnDeviceReconfigurationRules(emptySet())

    fun <P : Any, E : EventProducer<P>> condition(
        event: E,
        predicate: (P) -> Boolean,
    ): EventReconfiguration<P, E> = EventReconfiguration(event, predicate)

    infix fun <P : Any, E : EventProducer<P>> EventReconfiguration<P, E>.reconfigures(
        reconfiguration: ReconfigurationComponentScope.() -> Unit,
    ) {
        rules = OnDeviceReconfigurationRules(rules.rules + this)
    }
}
