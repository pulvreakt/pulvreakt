package it.unibo.pulvreakt.dsl

import arrow.core.Either
import arrow.core.NonEmptyList
import it.unibo.pulvreakt.dsl.errors.ConfigurationError
import it.unibo.pulvreakt.dsl.model.PulvreaktConfiguration

/**
 * Entrypoint for the pulverization DSL used to configure the pulverization system.
 *
 * ```kotlin
 * val configurationResult = pulverization {
 *   val device by logicDevice {
 *     withBehaviour<DeviceBehaviour>() requires setOf(embeddedDevice, highCpu)
 *     withSensors<DeviceSensors>() requires embeddedDevice
 *     withCommunication<DeviceCommunication>() requires setOf(embeddedDevice, highCpu)
 *   }
 *   deployment(infrastructure, MqttProtocol()) {
 *     device(device) {
 *       DeviceBehaviour() startsOn server
 *       DeviceSensors() startsOn smartphone
 *       DeviceCommunication() startsOn smartphone
 *     }
 *   }
 * }
 * ```
 */
fun pulverization(config: PulverizationScope.() -> Unit): Either<NonEmptyList<ConfigurationError>, PulvreaktConfiguration> =
    PulverizationScope().apply(config).generate()
