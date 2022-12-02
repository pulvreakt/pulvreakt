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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.Serializable

@Serializable
data class StatePayload(val i: Int) : StateRepresentation

@Serializable
data class CommPayload(val i: Int) : CommunicationPayload

@Serializable
object NoPayload

class FixtureBehaviour : Behaviour<StatePayload, CommPayload, NoPayload, NoPayload, Unit> {
    override val context: Context
        get() = TODO("Not yet implemented")

    override fun invoke(
        state: StatePayload,
        export: List<CommPayload>,
        sensedValues: NoPayload,
    ): BehaviourOutput<StatePayload, CommPayload, NoPayload, Unit> = TODO("Not yet implemented")
}

class StateFixture : State<StatePayload> {
    override val context: Context
        get() = TODO("Not yet implemented")

    override fun get(): StatePayload = TODO("Not yet implemented")
    override fun update(newState: StatePayload): StatePayload = TODO("Not yet implemented")
}

class CommunicationFixture : Communication<CommPayload> {
    override val context: Context
        get() = TODO("Not yet implemented")

    override fun send(payload: CommPayload) = TODO("Not yet implemented")
    override fun receive(): Flow<CommPayload> = TODO("Not yet implemented")
}

class RemoteCommunicator : Communicator {
    override suspend fun setup(binding: Binding) {}
    override suspend fun fireMessage(message: ByteArray) {}
    override fun receiveMessage(): Flow<ByteArray> = emptyFlow()
}
