package it.nicolasfarabegoli.pulverization.example01

import com.uchuhimo.konf.ConfigSpec

data class LogicalDevice(val id: String, val neighbours: Set<String>)

object PulverizationConfig : ConfigSpec("pulverization") {
    val devices by required<List<LogicalDevice>>(description = "List of Logical devices")
}
