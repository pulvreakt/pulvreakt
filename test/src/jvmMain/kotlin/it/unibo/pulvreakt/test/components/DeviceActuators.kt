package it.unibo.pulvreakt.test.components

import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.core.component.pulverisation.Actuators
import kotlinx.serialization.serializer

class DeviceActuators : Actuators<Int>(serializer()) {
    private val logger = KotlinLogging.logger("DeviceActuators")
    override suspend fun actuate(actuation: Int) {
        logger.info { "Actuating with $actuation" }
    }
}
