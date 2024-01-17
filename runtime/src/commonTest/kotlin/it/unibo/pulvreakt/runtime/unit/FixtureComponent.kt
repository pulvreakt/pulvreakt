package it.unibo.pulvreakt.runtime.unit

import arrow.core.Either
import it.unibo.pulvreakt.api.communication.Mode
import it.unibo.pulvreakt.api.communication.protocol.Entity
import it.unibo.pulvreakt.api.communication.protocol.Protocol
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.component.pulverization.Behavior
import it.unibo.pulvreakt.api.component.pulverization.BehaviourOutput
import it.unibo.pulvreakt.api.component.pulverization.GetState
import it.unibo.pulvreakt.api.component.pulverization.SetState
import it.unibo.pulvreakt.api.component.pulverization.State
import it.unibo.pulvreakt.api.component.pulverization.StateOps
import it.unibo.pulvreakt.api.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.api.scheduler.ExecutionScheduler
import it.unibo.pulvreakt.errors.protocol.ProtocolError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.serializer
import org.kodein.di.DI

class Infinite : ExecutionScheduler {
    override fun timesSequence(): Sequence<Long> = generateSequence { 0L }
}

class FixtureBehavior : Behavior<Int, Int, Unit, Unit, Unit>(
    Infinite(),
    serializer(),
    serializer(),
    serializer(),
    serializer(),
) {
    override fun invoke(state: Int?, comm: List<Unit>, sensors: Unit?): BehaviourOutput<Int, Unit, Unit> {
        return BehaviourOutput(0, Unit, Unit)
    }
}

class FixtureState : State<Int, Int>(serializer()) {
    override fun queryState(query: StateOps<Int>): Int {
        return when (query) {
            is GetState -> 1
            is SetState -> 1
        }
    }
}

class TestProtocol : Protocol {
    override lateinit var di: DI

    override fun setupInjector(kodein: DI) {
        di = kodein
    }

    override suspend fun setupChannel(source: Entity, destination: Entity) {
        TODO("Not yet implemented")
    }

    override suspend fun writeToChannel(from: Entity, to: Entity, message: ByteArray): Either<ProtocolError, Unit> {
        TODO("Not yet implemented")
    }

    override fun readFromChannel(from: Entity, to: Entity): Either<ProtocolError, Flow<ByteArray>> {
        TODO("Not yet implemented")
    }

    override suspend fun initialize(): Either<ProtocolError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun finalize(): Either<ProtocolError, Unit> {
        TODO("Not yet implemented")
    }
}

class TestComponentModeReconfigurator : ComponentModeReconfigurator {
    private val _flow = MutableSharedFlow<Pair<ComponentRef, Mode>>(1)

    override fun receiveModeUpdates(): Flow<Pair<ComponentRef, Mode>> = _flow.asSharedFlow()
    override suspend fun setMode(component: ComponentRef, mode: Mode) = _flow.emit(component to mode)
}
