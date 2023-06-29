package it.unibo.pulvreakt.core.dsl.fixture

import arrow.core.Either
import arrow.core.right
import it.unibo.pulvreakt.core.component.AbstractComponent

class TestComponent1 : AbstractComponent() {
    override suspend fun execute(): Either<String, Unit> = Unit.right()
}

class TestComponent2 : AbstractComponent() {
    override suspend fun execute(): Either<String, Unit> = Unit.right()
}
