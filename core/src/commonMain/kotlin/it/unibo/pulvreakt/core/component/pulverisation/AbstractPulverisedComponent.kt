package it.unibo.pulvreakt.core.component.pulverisation

import arrow.core.Either
import arrow.core.toOption
import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.component.errors.ComponentError

abstract class AbstractPulverisedComponent : AbstractComponent() {
    protected fun getComponentByType(type: ComponentType): Either<ComponentError.ExecutionError, ComponentRef> {
        return this.links.firstOrNull { it.type == type }.toOption().toEither {
            ComponentError.ExecutionError(
                """
                    No component of type $type found".
                    This is an internal error, please fill an issue to https://github.com/pulvreakt/pulvreakt.
                """.trimIndent(),
            )
        }
    }
}
