package it.unibo.pulvreakt.modularization.api.deployment.network

sealed interface NetworkMessage
data class MessageReceived(val host: String, val messages: MessagePayload<*>) : NetworkMessage
data class MessageSent(val host: String, val messages: MessagePayload<SingleMessage<*>>) : NetworkMessage
data class MoveComponentExecution(val from: String, val to: String, val component: String, val id: Int) : NetworkMessage
data class BootstrapComponentExecution(val host: String, val component: String, val id: Int) : NetworkMessage

data class SingleMessage<Payload>(val default: Payload, val overrides: Map<Pair<String, Int>, Payload> = emptyMap())
data class MessagePayload<Payload>(val host: String, val id: Int, val payload: Payload)
