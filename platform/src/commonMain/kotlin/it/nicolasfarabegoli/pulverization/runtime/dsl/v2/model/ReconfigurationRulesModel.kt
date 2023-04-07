package it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model

import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface ReconfigurationEvent<P : Any> {
    companion object {
        operator fun <P : Any> invoke(events: Flow<P>, predicate: (P) -> Boolean): ReconfigurationEvent<P> =
            ReconfigurationEventImpl(events, predicate)
    }

    val events: Flow<P>
    val predicate: (P) -> Boolean

    private data class ReconfigurationEventImpl<P : Any>(
        override val events: Flow<P>,
        override val predicate: (P) -> Boolean,
    ) : ReconfigurationEvent<P>
}

data class DeviceReconfigurationRule(
    val rule: ReconfigurationEvent<*>,
    val reconfiguration: Pair<ComponentType, Host>,
)

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
