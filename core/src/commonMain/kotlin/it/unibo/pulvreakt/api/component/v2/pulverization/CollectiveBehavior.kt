package it.unibo.pulvreakt.api.component.v2.pulverization

import it.unibo.collektive.networking.InboundMessage
import it.unibo.collektive.state.State

data class CollectiveInput<ID : Any>(
    val state: State,
    val messages: Iterable<InboundMessage<ID>>
)

