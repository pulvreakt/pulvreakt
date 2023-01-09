package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Actuator
import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.BehaviourOutput
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.Sensor
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.runtime.communication.Binding
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.Serializable

@Serializable
data class StatePayload(val i: Int)

@Serializable
data class CommPayload(val i: Int)

class FixtureBehaviour : Behaviour<StatePayload, CommPayload, Int, Double, Unit> {
    override val context: Context
        get() = TODO("Not yet implemented")

    override fun invoke(
        state: StatePayload,
        export: List<CommPayload>,
        sensedValues: Int,
    ): BehaviourOutput<StatePayload, CommPayload, Double, Unit> =
        BehaviourOutput(state, CommPayload(2), 1.0, Unit)
}

class StateFixture : State<StatePayload> {
    private var state = StatePayload(0)
    override val context: Context
        get() = TODO("Not yet implemented")

    override fun get(): StatePayload = state
    override fun update(newState: StatePayload): StatePayload {
        val tmp = state
        state = newState
        return tmp
    }
}

class CommunicationFixture : Communication<CommPayload> {
    override val context: Context
        get() = TODO("Not yet implemented")

    override suspend fun send(payload: CommPayload) {}
    override fun receive(): Flow<CommPayload> = emptyFlow()
}

class DeviceActuator : Actuator<Double> {
    override suspend fun actuate(payload: Double) {
        TODO("Not yet implemented")
    }
}

class DeviceActuatorContainer : ActuatorsContainer() {
    override val context: Context
        get() = TODO("Not yet implemented")

    override suspend fun initialize() {
        val actuator = DeviceActuator().apply { initialize() }
        this += actuator
    }
}

class DeviceSensor : Sensor<Int> {
    override suspend fun sense(): Int {
        TODO("Not yet implemented")
    }
}

class DeviceSensorContainer : SensorsContainer() {
    override val context: Context
        get() = TODO("Not yet implemented")

    override suspend fun initialize() {
        val sensor = DeviceSensor().apply { initialize() }
        this += sensor
    }
}

class RemoteCommunicator(private val comm: MutableSharedFlow<ByteArray>) : Communicator {
    override suspend fun setup(binding: Binding, remotePlace: RemotePlace?) {}
    override suspend fun finalize() {}
    override suspend fun fireMessage(message: ByteArray) = comm.emit(message)
    override fun receiveMessage(): Flow<ByteArray> = comm.asSharedFlow()
}
