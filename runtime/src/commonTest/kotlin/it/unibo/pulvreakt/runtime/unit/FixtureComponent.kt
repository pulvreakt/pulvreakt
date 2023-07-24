package it.unibo.pulvreakt.runtime.unit

import arrow.core.Either
import it.unibo.pulvreakt.core.communicator.Mode
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.pulverisation.Behaviour
import it.unibo.pulvreakt.core.component.pulverisation.BehaviourOutput
import it.unibo.pulvreakt.core.component.pulverisation.GetState
import it.unibo.pulvreakt.core.component.pulverisation.SetState
import it.unibo.pulvreakt.core.component.pulverisation.State
import it.unibo.pulvreakt.core.component.pulverisation.StateOps
import it.unibo.pulvreakt.core.component.time.TimeDistribution
import it.unibo.pulvreakt.core.protocol.Entity
import it.unibo.pulvreakt.core.protocol.Protocol
import it.unibo.pulvreakt.core.protocol.errors.ProtocolError
import it.unibo.pulvreakt.core.reconfiguration.component.ComponentModeReconfigurator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.serializer
import org.kodein.di.DI

class Infinite : TimeDistribution {
    override fun nextTimeInstant(): Long = 0L
    override fun isCompleted(): Boolean = false
}

class FixtureBehaviour : Behaviour<Int, Unit, Unit, Unit>(
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

class FixtureState : State<Int>(serializer()) {
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

    override suspend fun setupChannel(entity: Entity) {
        TODO("Not yet implemented")
    }

    override suspend fun writeToChannel(to: Entity, message: ByteArray): Either<ProtocolError, Unit> {
        TODO("Not yet implemented")
    }

    override fun readFromChannel(from: Entity): Either<ProtocolError, Flow<ByteArray>> {
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
