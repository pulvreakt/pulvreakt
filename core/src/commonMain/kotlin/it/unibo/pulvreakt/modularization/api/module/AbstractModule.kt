package it.unibo.pulvreakt.modularization.api.module

import arrow.core.raise.either
import it.unibo.pulvreakt.modularization.api.Capabilities

/**
 * Implementation of [Module] that provides a default implementation of [execute] method.
 */
abstract class AbstractModule<out Cap : Capabilities, Input, Output> : Module<Cap, Input, Output> {
    override fun execute(): ModuleResult<Output> = either {
        val inputs = buildInput().bind()
        invoke(inputs)
    }
}
