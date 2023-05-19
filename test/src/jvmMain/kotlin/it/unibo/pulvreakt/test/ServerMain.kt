package it.unibo.pulvreakt.test

import arrow.core.Either
import arrow.core.raise.either
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.runtime.PulvreaktRuntime

var runtime: PulvreaktRuntime? = null

suspend fun main() {
    val logger = KotlinLogging.logger("ServerMain")

    logger.info { "Server starting" }

    val result = either {
        val spec = systemSpec.bind()
        runtime = PulvreaktRuntime(spec, "device", 1, server).bind()
        runtime?.start()?.bind()
    }

    when (result) {
        is Either.Left -> {
            runtime?.stop()
            logger.error { result.value }
        }
        is Either.Right -> logger.info { "Server shutdown" }
    }
}
