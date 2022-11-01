package it.nicolasfarabegoli.pulverization.rabbitmq

import it.nicolasfarabegoli.pulverization.config.get
import it.nicolasfarabegoli.pulverization.core.DeviceIDOps.toID
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config.pulverizationSetup
import it.nicolasfarabegoli.pulverization.rabbitmq.components.BehaviourComponent
import it.nicolasfarabegoli.pulverization.rabbitmq.config.configuration
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceBehaviour
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceState
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val deviceID = System.getenv("DEVICE_ID")?.toID() ?: error("No `DEVICE_ID` variable found")
    pulverizationSetup(deviceID, configuration) {
        registerComponent<DeviceBehaviour>(configuration["device"])
        registerComponent<DeviceState>(configuration["device"])
    }

    val behaviour = BehaviourComponent()
    behaviour.initialize()
    behaviour.cycle()
}
