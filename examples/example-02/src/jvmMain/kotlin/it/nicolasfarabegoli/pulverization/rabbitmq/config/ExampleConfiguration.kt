package it.nicolasfarabegoli.pulverization.rabbitmq.config

import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config.pulverizationConfig
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceBehaviour
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceCommunication
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceSensorsContainer

val configuration = pulverizationConfig {
    logicalDevice("device") {
        component(DeviceBehaviour())
        component(DeviceCommunication())
        component(DeviceSensorsContainer())
    }
    rabbitmq {
        setHostname("localhost")
    }
}
