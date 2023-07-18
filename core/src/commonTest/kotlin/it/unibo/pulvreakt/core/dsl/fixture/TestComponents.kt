package it.unibo.pulvreakt.core.dsl.fixture

import arrow.core.Either
import arrow.core.right
import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.errors.ComponentError
import it.unibo.pulvreakt.core.component.fixture.SimpleTimeDistribution
import it.unibo.pulvreakt.core.component.pulverisation.Behaviour
import it.unibo.pulvreakt.core.component.pulverisation.BehaviourOutput
import it.unibo.pulvreakt.core.component.pulverisation.Communication
import it.unibo.pulvreakt.core.component.pulverisation.CommunicationPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.serializer

class TestComponent1 : AbstractComponent() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return !(other == null || this::class != other::class)
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}

class TestComponent2 : AbstractComponent() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return !(other == null || this::class != other::class)
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}

class CommTest : Communication<Unit>(serializer()) {
    override suspend fun send(message: CommunicationPayload<Unit>) {
        TODO("Not yet implemented")
    }

    override suspend fun receive(): Flow<CommunicationPayload<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun execute(): Either<ComponentError, Unit> {
        TODO("Not yet implemented")
    }
}

class BehaviourTest : Behaviour<Unit, Unit, Unit, Unit>(SimpleTimeDistribution(), serializer(), serializer(), serializer(), serializer()) {
    override fun invoke(state: Unit, comm: List<Unit>, sensors: Unit): BehaviourOutput<Unit, Unit, Unit> {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return !(other == null || this::class != other::class)
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}
