package it.unibo.pulvreakt.dsl.system

import arrow.core.Either
import arrow.core.NonEmptyList
import it.unibo.pulvreakt.dsl.system.errors.SystemDslError
import it.unibo.pulvreakt.dsl.system.model.SystemSpecification

/**
 * DSL entrypoint for the system definition.
 */
fun pulverizedSystem(config: SystemSpecificationScope.() -> Unit): Either<NonEmptyList<SystemDslError>, SystemSpecification> {
    val scope = SystemSpecificationScope().apply(config)
    return scope.generate()
}
