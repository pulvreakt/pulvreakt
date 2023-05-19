package it.unibo.pulvreakt.test

import arrow.core.nonEmptySetOf
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.dsl.pulverization
import it.unibo.pulvreakt.mqtt.MqttProtocol
import it.unibo.pulvreakt.test.components.DeviceActuators
import it.unibo.pulvreakt.test.components.DeviceBehaviour
import it.unibo.pulvreakt.test.components.DeviceSensors
import it.unibo.pulvreakt.test.components.Sens

val highCpu by Capability
val embeddedDevice by Capability

val server = Host("server", highCpu)
val smartphone = Host("smartphone", embeddedDevice)
val infrastructure = nonEmptySetOf(server, smartphone)

val systemSpec = pulverization {
    system {
        logicDevice<Unit, Unit, Sens, Int>("device") {
            withBehaviour<DeviceBehaviour>() requires highCpu
            withSensors<DeviceSensors>() requires embeddedDevice
            withActuators<DeviceActuators>() requires embeddedDevice
        }
    }

    deployment(infrastructure, MqttProtocol("test.mosquitto.org")) {
        device("device") {
            DeviceBehaviour() startsOn server
            DeviceSensors() startsOn smartphone
            DeviceActuators() startsOn smartphone
        }
    }
}
