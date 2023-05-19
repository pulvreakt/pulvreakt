package it.unibo.pulvreakt.test.components

import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.core.component.pulverisation.Behaviour
import it.unibo.pulvreakt.core.component.pulverisation.BehaviourOutput
import it.unibo.pulvreakt.test.ConstantDistribution
import kotlinx.serialization.serializer
import kotlin.random.Random

class DeviceBehaviour : Behaviour<Unit, Unit, Sens, Int>(
    ConstantDistribution(1000),
    serializer(),
    serializer(),
    serializer(),
    serializer(),
) {
    private val logger = KotlinLogging.logger("DeviceBehaviour")

    override fun invoke(state: Unit?, comm: List<Unit>, sensors: Sens?): BehaviourOutput<Unit, Unit, Int> {
        logger.info { "last sensor state: $sensors" }
        logger.info { "Fake computing something..." }
        return BehaviourOutput(Unit, Unit, Random.nextInt())
    }
}
