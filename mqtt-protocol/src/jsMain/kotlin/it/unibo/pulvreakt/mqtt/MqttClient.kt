@file:Suppress("REDUNDANT_NULLABLE")
@file:JsModule("mqtt")
@file:JsNonModule

package it.unibo.pulvreakt.mqtt

/**
 * Represents a MQTT client.
 */
external class MqttClient {

    /**
     * Publishes a message on the given topic.
     * @param topic The topic.
     * @param message The message.
     * @param options The message options (qos, retain, ...).
     * @param callback The function to be called upon completion.
     */
    fun publish(
        topic: String,
        message: dynamic,
        options: dynamic? = definedExternally,
        callback: ((err: dynamic) -> Unit)? = definedExternally
    )

    /**
     * Subscribes to the given topic.
     * @param topic The topic.
     * @param options The message options (qos, retain, ...).
     * @param callback The function to be executed upon completion.
     */
    fun subscribe(
        topic: String,
        options: dynamic? = definedExternally,
        callback: ((err: dynamic, granted: dynamic) -> Unit)? = definedExternally
    )

    /**
     * Sets up what to do on certain events; this is to be used for the message event.
     * @param event The trigger for the callback function (message, connect, ...).
     * @param callback The function to be executed after the given event.
     */
    fun on(event: String, callback: (String, dynamic, dynamic) -> Unit)

    /**
     * Sets up what to do on certain events; this is to be used for other events.
     * @param event The trigger for the callback function (message, connect, ...).
     * @param callback The function to be executed after the given event.
     */
    fun on(event: String, callback: (dynamic) -> Unit)

    /**
     * Closes the client.
     * @param force The end needs to be forced or not.
     * @param options Additional options.
     * @param callback The function to be called upon completion.
     */
    fun end(
        force: Boolean? = definedExternally,
        options: dynamic? = definedExternally,
        callback: (() -> Unit)? = definedExternally
    )
}

/**
 * Connects to the broker.
 * @return The client [MqttClient].
 */
external fun connect(brokerUrl: String, options: dynamic? = definedExternally): MqttClient
