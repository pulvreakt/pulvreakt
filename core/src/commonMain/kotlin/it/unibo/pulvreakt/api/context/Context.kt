package it.unibo.pulvreakt.api.context

import it.unibo.pulvreakt.api.infrastructure.Host

/**
 * Models the execution context on which a component is running.
 * Contains the information about the device id belonging to the component and the host where the system is running.
 * Metadata can be added and queried to the context to provide additional application-specific information.
 *
 * ```kotlin
 * val context: Context = Context(1.toId(), host)
 * val value: Int? = context["key"] // returns null
 * context["key"] = 1
 * val value: Int? = context["key"] // returns 1
 * val invalid: Double? = context["key"] // returns null
 * ```
 */
interface Context<ID : Any> {
    /**
     * The [ID] of the _Logical Device_ managed by the instance of the middleware.
     */
    val deviceId: ID

    /**
     * The [Host] where the system is running.
     */
    val host: Host

    /**
     * Returns the value associated with the given [key] or `null` if no value is associated with the given [key].
     * The method return `null` if the type of the value is not the same as the type of the variable.
     *
     * ```kotlin
     * val context: Context = Context(1.toId(), host)
     * val value: Int? = context["key"]
     * val value: Int? = context["key"] ?: 0 // default value if null
     * val ping: Double = context["ping"]
     * ```
     */
    operator fun <T : Any> get(key: String): T?

    /**
     * Associates the specified [value] with the specified [key] in the context.
     * If the context previously contained a value associated with the [key], the old value is replaced by the specified [value].
     *
     * ```kotlin
     * val context: Context = Context(1.toId(), host)
     * context["key"] = 1
     * ```
     */
    operator fun <T : Any> set(
        key: String,
        value: T,
    )

    companion object {
        /**
         * Creates a new [Context] with the given [deviceId] and [host].
         */
        operator fun <ID : Any> invoke(
            deviceId: ID,
            host: Host,
        ): Context<ID> =
            object : Context<ID> {
                private val metadata: MutableMap<String, Any> = mutableMapOf()
                override val deviceId: ID = deviceId
                override val host: Host = host

                @Suppress("UNCHECKED_CAST")
                override fun <T : Any> get(key: String): T? = metadata[key] as? T

                override fun <T : Any> set(
                    key: String,
                    value: T,
                ) {
                    metadata[key] = value
                }
            }
    }
}
