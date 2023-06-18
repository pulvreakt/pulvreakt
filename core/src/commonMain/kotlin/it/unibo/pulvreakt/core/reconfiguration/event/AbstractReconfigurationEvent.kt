package it.unibo.pulvreakt.core.reconfiguration.event

import arrow.core.right
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Represents an event source that can trigger a reconfiguration.
 * Additionally, it provides specific handlers for each [ReconfigurationResult].
 */
abstract class AbstractReconfigurationEvent<T : Any> : ReconfigurationEvent<T> {
    private val internalResultFlow = MutableSharedFlow<ReconfigurationResult<T>>()

    final override fun resultsFlow(): Flow<ReconfigurationResult<T>> = internalResultFlow.asSharedFlow()

    final override suspend fun onProcessedEvent(result: ReconfigurationResult<T>) {
        internalResultFlow.emit(result)
        when (result) {
            is ReconfigurationResult.ReconfigurationSuccess -> onReconfigurationSuccess(result)
            is ReconfigurationResult.SkipCheck -> onSkipCheck(result)
            is ReconfigurationResult.FailOnReconfiguration<T, *> -> onFailOnReconfiguration(result)
        }
    }

    override suspend fun initialize() = Unit.right()

    override suspend fun finalize() = Unit.right()

    /**
     * Function called by the framework when a reconfiguration is correctly performed.
     */
    open fun onReconfigurationSuccess(result: ReconfigurationResult.ReconfigurationSuccess<T>) = Unit

    /**
     * Function called by the framework when a reconfiguration is skipped.
     */
    open fun onSkipCheck(result: ReconfigurationResult.SkipCheck<T>) = Unit

    /**
     * Function called by the framework when a reconfiguration fails.
     */
    open fun onFailOnReconfiguration(result: ReconfigurationResult.FailOnReconfiguration<T, *>) = Unit
}
