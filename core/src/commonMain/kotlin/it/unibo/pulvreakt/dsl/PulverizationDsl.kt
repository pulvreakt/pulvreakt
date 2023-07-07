package it.unibo.pulvreakt.dsl

import arrow.core.Either
import arrow.core.NonEmptyList
import it.unibo.pulvreakt.dsl.errors.ConfigurationError
import it.unibo.pulvreakt.dsl.model.PulvreaktConfiguration

/**
 * Entrypoint for the pulverization DSL.
 */
fun pulverization(config: PulverizationScope.() -> Unit): Either<NonEmptyList<ConfigurationError>, PulvreaktConfiguration> =
    PulverizationScope().apply(config).generate()
