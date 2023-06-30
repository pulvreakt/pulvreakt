package it.unibo.pulvreakt.core.dsl.fixture

import arrow.core.Either
import arrow.core.right
import it.unibo.pulvreakt.core.component.AbstractComponent

class TestComponent1 : AbstractComponent() {
    override suspend fun execute(): Either<String, Unit> = Unit.right()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return !(other == null || this::class != other::class)
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}

class TestComponent2 : AbstractComponent() {
    override suspend fun execute(): Either<String, Unit> = Unit.right()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return !(other == null || this::class != other::class)
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}
