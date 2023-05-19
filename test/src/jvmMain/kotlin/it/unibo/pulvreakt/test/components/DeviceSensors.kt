package it.unibo.pulvreakt.test.components

import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.core.component.pulverisation.Sensors
import it.unibo.pulvreakt.test.ConstantDistribution
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.random.Random

@Serializable
data class Sens(val value: Double)

class DeviceSensors : Sensors<Sens>(ConstantDistribution(500), serializer()) {
    private val logger = KotlinLogging.logger("DeviceSensors")

    override suspend fun sense(): Sens {
        logger.info { "new sensing..." }
        return Sens(Random.nextDouble())
    }
}
