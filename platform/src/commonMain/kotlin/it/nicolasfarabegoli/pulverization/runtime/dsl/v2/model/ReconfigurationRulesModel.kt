package it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model

import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Represents the event that can trigger a reconfiguration.
 */
interface ReconfigurationEvent<P : Any> {
    companion object {
        /**
         * Utility method for creating an [ReconfigurationEvent] from [events] and a [predicate] which should
         * be true to trigger the reconfiguration.
         */
        operator fun <P : Any> invoke(events: Flow<P>, predicate: (P) -> Boolean): ReconfigurationEvent<P> =
            ReconfigurationEventImpl(events, predicate)
    }

    /**
     * The [Flow] of [events] that could trigger the reconfiguration.
     */
    val events: Flow<P>

    /**
     * The predicate that should be true to trigger the reconfiguration.
     */
    val predicate: (P) -> Boolean

    private data class ReconfigurationEventImpl<P : Any>(
        override val events: Flow<P>,
        override val predicate: (P) -> Boolean,
    ) : ReconfigurationEvent<P>
}

/**
 * A single device [reconfiguration] [rule].
 */
data class DeviceReconfigurationRule(
    val rule: ReconfigurationEvent<*>,
    val reconfiguration: Pair<ComponentType, Host>,
)

/**
 * All the [deviceReconfigurationRules].
 */
data class ReconfigurationRules(
    val deviceReconfigurationRules: Set<DeviceReconfigurationRule>,
    // val externalReconfigurationRules: DeviceReconfigurationRule
)

internal fun startOnDeviceReconfigurationRules(scope: CoroutineScope, rules: Set<ReconfigurationEvent<*>>): Set<Job> {
    fun <P : Any> ReconfigurationEvent<P>.spawnRule(): Job {
        return scope.launch {
            events.collect {
                // TODO(verify in the if condition if the rule should be checked)
                if (predicate(it)) {
                    TODO("Reconfigure the deployment unit is missing")
                }
            }
        }
    }
    return rules.map { it.spawnRule() }.toSet()
}
