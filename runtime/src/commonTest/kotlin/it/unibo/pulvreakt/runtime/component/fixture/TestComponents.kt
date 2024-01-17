package it.unibo.pulvreakt.runtime.component.fixture

import arrow.core.Either
import arrow.core.right
import it.unibo.pulvreakt.api.component.AbstractComponent
import it.unibo.pulvreakt.api.component.pulverization.Actuators
import it.unibo.pulvreakt.api.component.pulverization.Behaviour
import it.unibo.pulvreakt.api.component.pulverization.BehaviourOutput
import it.unibo.pulvreakt.api.component.pulverization.Communication
import it.unibo.pulvreakt.api.component.pulverization.CommunicationPayload
import it.unibo.pulvreakt.api.component.pulverization.GetState
import it.unibo.pulvreakt.api.component.pulverization.Sensors
import it.unibo.pulvreakt.api.component.pulverization.SetState
import it.unibo.pulvreakt.api.component.pulverization.State
import it.unibo.pulvreakt.api.component.pulverization.StateOps
import it.unibo.pulvreakt.errors.component.ComponentError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.serializer

class TestSensorsComponent : AbstractComponent() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
}

class TestBehaviour : Behaviour<Int, Unit, Unit, Unit>(
    FiniteShotExecutionScheduler(2),
    serializer(),
    serializer(),
    serializer(),
    serializer(),
) {
    override fun invoke(state: Int?, comm: List<Unit>, sensors: Unit?): BehaviourOutput<Int, Unit, Unit> {
        return BehaviourOutput(1, Unit, Unit)
    }
}

class TestState : State<Int>(serializer()) {
    override fun queryState(query: StateOps<Int>): Int {
        return when (query) {
            is GetState -> 1
            is SetState -> 1
        }
    }
}

class TestSensors : Sensors<Unit>(FiniteShotExecutionScheduler(1), serializer()) {
    override suspend fun sense() = Unit
}

class TestActuators : Actuators<Unit>(serializer()) {
    override suspend fun actuate(actuation: Unit) = Unit
}

class TestCommunication : Communication<Unit>(serializer()) {
    override suspend fun send(message: CommunicationPayload<Unit>) = Unit
    override suspend fun receive(): Flow<CommunicationPayload<Unit>> = emptyFlow()
}
