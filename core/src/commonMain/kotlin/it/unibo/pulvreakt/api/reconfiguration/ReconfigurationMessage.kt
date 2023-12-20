package it.unibo.pulvreakt.api.reconfiguration

import kotlinx.serialization.Serializable

/**
 * Represents a new [configuration] of the deployment unit provided by the [sourceHost].
 * The [configuration] is modelled via a pair (component name, host name).
 *
 * ```
 * Component1 to Local
 * Component2 to Remote
 * Component3 to Local
 * ```
 */
@Serializable
data class ReconfigurationMessage(val sourceHost: String, val configuration: Pair<String, String>)
