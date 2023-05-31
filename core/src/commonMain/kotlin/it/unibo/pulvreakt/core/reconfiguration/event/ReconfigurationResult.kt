package it.unibo.pulvreakt.core.reconfiguration.event

/**
 * Represents the result of a reconfiguration.
 */
sealed interface ReconfigurationResult<T : Any> {
    /**
     * Represents a successful reconfiguration caused by the [event].
     */
    data class ReconfigurationSuccess<T : Any>(val event: T) : ReconfigurationResult<T>

    /**
     * Represents a reconfiguration that has not been triggered by the [event].
     */
    data class SkipCheck<T : Any>(val event: T) : ReconfigurationResult<T>

    /**
     * Represents a failed reconfiguration caused by the [event] with a [cause].
     */
    data class FailOnReconfiguration<T : Any, E : Throwable>(val event: T?, val cause: E) : ReconfigurationResult<T>
}
