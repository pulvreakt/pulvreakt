package it.unibo.pulvreakt.api.component.pulverization

import arrow.core.Either
import arrow.core.toOption
import it.unibo.pulvreakt.api.component.AbstractComponent
import it.unibo.pulvreakt.api.component.ComponentKind
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.errors.component.ComponentError

/**
 * Abstract class for components that are part of the pulverisation model in the strict setup.
 */
abstract class AbstractPulverizedComponent<ID : Any> : AbstractComponent<ID>() {
    protected fun getComponentByType(type: ComponentKind): Either<ComponentError.ExecutionError, ComponentRef> {
        return links.firstOrNull { it.type == type }.toOption().toEither {
            ComponentError.ExecutionError(
                """
                No component of type $type found".
                This is an internal error, please fill an issue to https://github.com/pulvreakt/pulvreakt.
                """.trimIndent(),
            )
        }
    }

    protected fun getComponentByTypeOrNull(type: ComponentKind): ComponentRef? {
        return links.firstOrNull { it.type == type }
    }
}
