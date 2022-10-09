package it.nicolasfarabegoli.pulverization.example01

import com.uchuhimo.konf.ConfigSpec

data class LogicalDevice(val id: String, val neighbours: Set<String>)

object PulverizationConfig : ConfigSpec("pulverization") {
    val hostname by optional(default = "rabbitmq", description = "The hostname of RabbitMQ")
    val port by optional(default = 5672, description = "The port of RabbitMQ")
    val repetitions by optional(default = -1, description = "Numbers of repetitions")
    val devices by required<List<LogicalDevice>>(description = "List of Logical devices")
}
