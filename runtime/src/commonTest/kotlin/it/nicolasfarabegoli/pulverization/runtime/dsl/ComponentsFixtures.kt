package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.BehaviourOutput
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.runtime.communication.Binding
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.Serializable

@Serializable
data class StatePayload(val i: Int) : StateRepresentation

@Serializable
data class CommPayload(val i: Int) : CommunicationPayload

class FixtureBehaviour : Behaviour<StatePayload, CommPayload, NoVal, NoVal, Unit> {
    override val context: Context
        get() = TODO("Not yet implemented")

    override fun invoke(
        state: StatePayload,
        export: List<CommPayload>,
        sensedValues: NoVal,
    ): BehaviourOutput<StatePayload, CommPayload, NoVal, Unit> =
        BehaviourOutput(state, CommPayload(2), NoVal, Unit)
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

    override fun send(payload: CommPayload) {}
    override fun receive(): Flow<CommPayload> = emptyFlow()
}

class RemoteCommunicator : Communicator {
    override suspend fun setup(binding: Binding, remotePlace: RemotePlace?) {}
    override suspend fun finalize() {}
    override suspend fun fireMessage(message: ByteArray) {}
    override fun receiveMessage(): Flow<ByteArray> = emptyFlow()
}
