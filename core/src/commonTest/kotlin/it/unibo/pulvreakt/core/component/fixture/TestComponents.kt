package it.unibo.pulvreakt.core.component.fixture

import arrow.core.Either
import arrow.core.right
import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.errors.ComponentError

class TestSensorsComponent : AbstractComponent() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
}
