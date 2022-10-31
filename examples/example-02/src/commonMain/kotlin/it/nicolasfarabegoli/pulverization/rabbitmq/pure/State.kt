package it.nicolasfarabegoli.pulverization.rabbitmq.pure

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import org.koin.core.component.inject

data class StateRepr(val int: Int) : StateRepresentation

class DeviceState : State<StateRepr> {
    override val context: Context by inject()

    private var internalState: StateRepr = StateRepr(0)

    override fun get(): StateRepr = internalState

    override fun update(newState: StateRepr): StateRepr {
        val tmp = internalState
        internalState = newState
        return tmp
    }
}
