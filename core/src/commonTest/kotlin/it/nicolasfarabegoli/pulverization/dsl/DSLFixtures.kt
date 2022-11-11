package it.nicolasfarabegoli.pulverization.dsl

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.BehaviourOutput
import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import it.nicolasfarabegoli.pulverization.core.DeviceID
import it.nicolasfarabegoli.pulverization.core.DeviceIDOps.toID
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.core.StateRepresentation

interface DSLFixtures {
    data class StateRepr(val i: Int) : StateRepresentation
    object FuzzyContext : Context {
        override val id: DeviceID = "1".toID()
    }

    data class MyState(override val context: Context = FuzzyContext) : State<StateRepr> {
        override fun get(): StateRepr {
            TODO("Not yet implemented")
        }

        override fun update(newState: StateRepr): StateRepr {
            TODO("Not yet implemented")
        }
    }

    class MyState2 : State<StateRepr> {
        override val context: Context = FuzzyContext
        override fun get(): StateRepr {
            TODO("Not yet implemented")
        }

        override fun update(newState: StateRepr): StateRepr {
            TODO("Not yet implemented")
        }
    }

    data class Payload(val payload: String) : CommunicationPayload
    class MyBehaviour : Behaviour<StateRepr, Payload, Int, Int, Unit> {
        override val context: Context = FuzzyContext
        override fun invoke(
            state: StateRepr,
            export: List<Payload>,
            sensedValues: Int,
        ): BehaviourOutput<StateRepr, Payload, Int, Unit> {
            TODO("Not yet implemented")
        }
    }
}
