package it.unibo.pulvreakt.core.dsl.fixture

import arrow.core.Either
import arrow.core.right
import it.unibo.pulvreakt.api.component.AbstractComponent
import it.unibo.pulvreakt.api.component.pulverization.Actuators
import it.unibo.pulvreakt.api.component.pulverization.Behavior
import it.unibo.pulvreakt.api.component.pulverization.BehaviourOutput
import it.unibo.pulvreakt.api.component.pulverization.Communication
import it.unibo.pulvreakt.api.component.pulverization.CommunicationPayload
import it.unibo.pulvreakt.api.component.pulverization.Sensors
import it.unibo.pulvreakt.errors.component.ComponentError
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

class BehaviorTest : Behavior<Int, Unit, Unit, Unit>(
    TestScheduler(),
    serializer(),
    serializer(),
    serializer(),
    serializer(),
) {
    override fun invoke(state: Int?, comm: List<Unit>, sensors: Unit?): BehaviourOutput<Int, Unit, Unit> {
        return BehaviourOutput(1, Unit, Unit)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return !(other == null || this::class != other::class)
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}

class SensorsTest : Sensors<Unit>(TestScheduler(), serializer()) {
    override suspend fun sense() = Unit

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return !(other == null || this::class != other::class)
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}

class ActuatorsTest : Actuators<Unit>(serializer()) {
    override suspend fun actuate(actuation: Unit) {
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
