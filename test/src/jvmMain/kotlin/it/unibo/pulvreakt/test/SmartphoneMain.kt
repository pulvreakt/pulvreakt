package it.unibo.pulvreakt.test

import arrow.core.Either
import arrow.core.raise.either
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.runtime.PulvreaktRuntime

var smartphoneRuntime: PulvreaktRuntime? = null

suspend fun main() {
    val logger = KotlinLogging.logger("SmartphoneMain")

    logger.info { "Smartphone starting" }

    val result = either {
        val spec = systemSpec.bind()
        smartphoneRuntime = PulvreaktRuntime(spec, "device", 1, smartphone).bind()
        smartphoneRuntime?.start()?.bind()
    }

    when (result) {
        is Either.Left -> {
            smartphoneRuntime?.stop()
            logger.error { result.value }
        }
        is Either.Right -> logger.info { "Smartphone shutdown" }
    }
}
