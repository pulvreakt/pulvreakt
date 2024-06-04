@file:JsModule("mqtt")
@file:JsNonModule

package it.unibo.pulvreakt.mqtt

/**
 * Represents a MQTT client.
 */
external class MqttClient {

    /**
     * Publishes the [message] on the given [topic] with the given [options] and executing the [callback] function after ending.
     */
    fun publish(
        topic: String,
        message: dynamic,
        options: dynamic = definedExternally,
        callback: ((err: dynamic) -> Unit)? = definedExternally
    )

    /**
     * Subscribes to the given [topic] with the given [options] and executing the [callback] function after ending.
     */
    fun subscribe(
        topic: String,
        options: dynamic = definedExternally,
        callback: ((err: dynamic, granted: dynamic) -> Unit)? = definedExternally
    )

    /**
     * Sets up to execute the [callback] function after the trigger of the given [event]
     */
    fun on(event: String, callback: (String, dynamic, dynamic) -> Unit)

    /**
     * Sets up to execute the [callback] function after the trigger of the given [event]
     */
    fun on(event: String, callback: (dynamic) -> Unit)

    /**
     * Closes the client with the given [options] and executing the [callback] function after ending.
     * can be forced by setting the [force] flag to true.
     */
    fun end(
        force: Boolean? = definedExternally,
        options: dynamic = definedExternally,
        callback: (() -> Unit)? = definedExternally
    )
}

/**
 * Connects to the broker.
 * @return The [MqttClient].
 */
external fun connect(brokerUrl: String, options: dynamic = definedExternally): MqttClient
