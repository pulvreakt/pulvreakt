package it.nicolasfarabegoli.pulverization.rabbitmq

import it.nicolasfarabegoli.pulverization.config.get
import it.nicolasfarabegoli.pulverization.core.DeviceIDOps.toID
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config.pulverizationSetup
import it.nicolasfarabegoli.pulverization.rabbitmq.components.DeviceSensorsComponent
import it.nicolasfarabegoli.pulverization.rabbitmq.config.configuration
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceSensorsContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val deviceID = System.getenv("DEVICE_ID")?.toID() ?: error("No `DEVICE_ID` variable found")
    pulverizationSetup(deviceID, configuration) {
        registerComponent<DeviceSensorsContainer>(configuration["device"])
    }

    val sensors = DeviceSensorsComponent()
    sensors.initialize()
    while (true) {
        sensors.cycle()
        delay(5000)
    }
}
