package it.unibo.pulvreakt.modularization.dsl

import arrow.core.Either
import arrow.core.Nel
import it.unibo.pulvreakt.modularization.api.module.Module
import it.unibo.pulvreakt.modularization.dsl.data.ModularizedSystem
import it.unibo.pulvreakt.modularization.dsl.errors.ConfigurationError

typealias ModularizationResult = Either<Nel<ConfigurationError>, ModularizedSystem>

/**
 * DSL entrypoint for specifying a modularization system.
 */
fun modularization(vararg modules: Module<*, *, *>, scope: ModularizationScope.() -> Unit): ModularizationResult =
    ModularizationScope(modules.toSet()).apply(scope).build()
