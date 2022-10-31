package it.nicolasfarabegoli.pulverization.rabbitmq

import it.nicolasfarabegoli.pulverization.config.get
import it.nicolasfarabegoli.pulverization.core.DeviceIDOps.toID
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config.pulverizationSetup
import it.nicolasfarabegoli.pulverization.rabbitmq.components.CommunicationComponent
import it.nicolasfarabegoli.pulverization.rabbitmq.config.configuration
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceCommunication
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val deviceID = System.getenv("DEVICE_ID")?.toID() ?: error("No `DEVICE_ID` variable found")
    pulverizationSetup(deviceID, configuration) {
        registerComponent<DeviceCommunication>(configuration["device"])
    }

    CommunicationComponent().initialize()
}
