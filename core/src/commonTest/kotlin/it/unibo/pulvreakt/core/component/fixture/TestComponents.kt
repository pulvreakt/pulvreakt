package it.unibo.pulvreakt.core.component.fixture

import arrow.core.Either
import arrow.core.right
import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.errors.ComponentError
import it.unibo.pulvreakt.core.component.pulverisation.Actuators
import it.unibo.pulvreakt.core.component.pulverisation.Behaviour
import it.unibo.pulvreakt.core.component.pulverisation.BehaviourOutput
import it.unibo.pulvreakt.core.component.pulverisation.Communication
import it.unibo.pulvreakt.core.component.pulverisation.CommunicationPayload
import it.unibo.pulvreakt.core.component.pulverisation.GetState
import it.unibo.pulvreakt.core.component.pulverisation.Sensors
import it.unibo.pulvreakt.core.component.pulverisation.SetState
import it.unibo.pulvreakt.core.component.pulverisation.State
import it.unibo.pulvreakt.core.component.pulverisation.StateOps
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.serializer

class TestSensorsComponent : AbstractComponent() {
    override suspend fun execute(): Either<ComponentError, Unit> = Unit.right()
}

class TestBehaviour : Behaviour<Int, Unit, Unit, Unit>(
    OneShotTimeDistribution(),
    serializer(),
    serializer(),
    serializer(),
    serializer(),
) {
    override fun invoke(state: Int, comm: List<Unit>, sensors: Unit): BehaviourOutput<Int, Unit, Unit> {
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

class TestSensors : Sensors<Unit>(OneShotTimeDistribution(), serializer()) {
    override suspend fun sense() = Unit
}

class TestActuators : Actuators<Unit>(serializer()) {
    override suspend fun actuate(actuation: Unit) = Unit
}

class TestCommunication : Communication<Unit>(serializer()) {
    override suspend fun send(message: CommunicationPayload<Unit>) = Unit
    override suspend fun receive(): Flow<CommunicationPayload<Unit>> = emptyFlow()
}
