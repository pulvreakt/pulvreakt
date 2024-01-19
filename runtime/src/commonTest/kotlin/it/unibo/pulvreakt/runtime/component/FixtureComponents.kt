package it.unibo.pulvreakt.runtime.component

import arrow.core.Either
import arrow.core.right
import it.unibo.pulvreakt.api.component.AbstractComponent
import it.unibo.pulvreakt.errors.component.ComponentError

class TestComponent1 : AbstractComponent<Int>() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
}

class TestComponent2 : AbstractComponent<Int>() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
}
