package it.unibo.pulvreakt.dsl.system

import arrow.core.Either
import it.unibo.pulvreakt.dsl.system.model.SystemSpecification

/**
 * DSL entrypoint for the system definition.
 */
fun pulverizedSystem(config: SystemSpecificationScope.() -> Unit): Either<String, SystemSpecification> {
    val scope = SystemSpecificationScope().apply(config)
    return scope.generate()
}
