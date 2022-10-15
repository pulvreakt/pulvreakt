package it.nicolasfarabegoli.pulverization.example01

import it.nicolasfarabegoli.pulverization.component.SendReceiveDeviceComponent
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.BehaviourOutput
import it.nicolasfarabegoli.pulverization.core.State
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent

@Serializable
data class Export(val deviceId: String, val sensors: Map<String, Double>)

typealias SensedValues = Map<String, Double>

class MyBehaviour(override val id: String) : Behaviour<String, Export, SensedValues, Unit, Unit, String> {
    override operator fun invoke(
        state: String,
        export: List<Export>,
        sensedValues: SensedValues,
    ): BehaviourOutput<String, Export, Unit, Unit> {
        return BehaviourOutput(state, Export(id, sensedValues), Unit, Unit) // Identity function
    }
}

class MyState(initialState: String) : State<String> {
    private var state: String = initialState

    override fun get(): String = state

    override fun update(newState: String): String {
        val oldState = state
        state = newState
        return oldState
    }
}

/**
 * All the messages handled by the [MyBehaviourComponent].
 * The messages types are represented as ADT.
 */
sealed class BehaviourOutgoingMessages {
    @Serializable
    data class CommunicationMessage(val export: Export) : BehaviourOutgoingMessages()

    @Serializable
    data class UpdateState(val newState: String) : BehaviourOutgoingMessages()
}

sealed class BehaviourIncomingMessages {
    data class GetState(val state: String) : BehaviourIncomingMessages()
    data class GetNeighboursExports(val exports: Map<String, Export>) : BehaviourIncomingMessages()
    data class GetSensedValues(val sensedValues: SensedValues) : BehaviourIncomingMessages()
}

expect class MyBehaviourComponent :
    SendReceiveDeviceComponent<BehaviourOutgoingMessages, BehaviourIncomingMessages, String>,
    KoinComponent
