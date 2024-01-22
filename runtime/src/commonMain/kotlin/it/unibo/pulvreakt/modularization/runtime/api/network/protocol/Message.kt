package it.unibo.pulvreakt.modularization.runtime.api.network.protocol

import it.unibo.pulvreakt.modularization.api.Host
import it.unibo.pulvreakt.modularization.api.module.SymbolicModule
import kotlin.jvm.JvmInline

/**
 * Type of protocol message.
 */
sealed interface Message

/**
 * Heartbeat message from an [host] sent at [timestamp].
 */
data class Heartbeat(val host: Host<*>, val timestamp: Long) : Message

/**
 * Offloading request of a [module] from a [sourceHost] to a [destinationHost] with the specified [id].
 * The [requestId] is used to implement the request-response pattern.
 */
data class OffloadModuleRequest(
    val sourceHost: Host<*>,
    val destinationHost: Host<*>,
    val module: SymbolicModule,
    val id: Int,
    val requestId: Long,
) : Message

/**
 * Acceptance of an offloading request with the specified [requestId].
 */
@JvmInline
value class OffloadAccepted(val requestId: Long) : Message

/**
 * Rejection of an offloading request with the specified [requestId].
 */
@JvmInline
value class OffloadRejected(val requestId: Long) : Message

/**
 * Outbound [message] sent [fromModule] with a [id] from a [sourceHost] at [timestamp].
 */
data class OutboundMessage<Msg : Any>(
    val sourceHost: Host<*>,
    val fromModule: SymbolicModule,
    val id: Int,
    val message: Msg,
    val timestamp: Long,
) : Message

/**
 * Inbound [message] sent [toModule] a module with a [id].
 */
data class InboundMessage<Msg : Any>(val toModule: SymbolicModule, val id: Int, val message: Msg) : Message
