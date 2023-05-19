package it.unibo.pulvreakt.runtime.dsl.model

import it.unibo.pulvreakt.runtime.reconfiguration.NewConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * The type of the possible results when an event is evaluated.
 */
sealed interface ReconfigurationResult<P : Any>

/**
 * A specialization of [ReconfigurationResult] representing a successful evaluation of the [event] that has
 * triggered a reconfiguration.
 */
data class ReconfigurationSuccess<P : Any>(val event: P) : ReconfigurationResult<P>

/**
 * A specialization of [ReconfigurationResult] representing a successful evaluation of the [event],
 * but that has not triggered a reconfiguration.
 */
data class SkipCheck<P : Any>(val event: P) : ReconfigurationResult<P>

/**
 * A specialization of [ReconfigurationResult] representing a failure when trying to reconfigure the deployment unit
 * triggered by the [event]. The [cause] represent the exception raised when a reconfiguration is tried.
 */
data class FailOnReconfiguration<P : Any, T : Throwable>(val event: P, val cause: T) : ReconfigurationResult<P>

/**
 * Represents the event that can trigger a reconfiguration.
 */
abstract class ReconfigurationEvent<P : Any> {
    private val internalResultFlow: MutableSharedFlow<ReconfigurationResult<P>> = MutableSharedFlow(1)

    companion object {
        /**
         * Utility method for creating an [ReconfigurationEvent] from [events] and a [predicate] which should
         * be true to trigger the reconfiguration.
         */
        operator fun <P : Any> invoke(events: Flow<P>, predicate: (P) -> Boolean): ReconfigurationEvent<P> =
            ReconfigurationEventImpl(events, predicate)
    }

    /**
     * The [Flow] of [ReconfigurationResult] associated to the processing of the [events].
     */
    val results: Flow<ReconfigurationResult<P>> = internalResultFlow.asSharedFlow()

    /**
     * The [Flow] of [events] that could trigger the reconfiguration.
     */
    abstract val events: Flow<P>

    /**
     * The predicate that should be true to trigger the reconfiguration.
     */
    abstract val predicate: (P) -> Boolean

    /**
     * Function called by the framework whenever an event is processed.
     * The function is called even if the processed event do not trigger a reconfiguration.
     * The function takes the [result] of the processing result
     */
    suspend fun onReconfigurationEvent(result: ReconfigurationResult<P>) {
        internalResultFlow.emit(result)
    }

    private data class ReconfigurationEventImpl<P : Any>(
        override val events: Flow<P>,
        override val predicate: (P) -> Boolean,
    ) : ReconfigurationEvent<P>()
}

/**
 * A single device [reconfiguration] [rule].
 */
data class DeviceReconfigurationRule(
    val rule: ReconfigurationEvent<*>,
    val reconfiguration: NewConfiguration,
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
