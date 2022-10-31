package it.nicolasfarabegoli.pulverization.rabbitmq.pure

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.BehaviourOutput
import org.koin.core.component.inject

class DeviceBehaviour : Behaviour<StateRepr, CommPayload, AllSensorsPayload, Unit, Unit> {
    override val context: Context by inject()

    override fun invoke(
        state: StateRepr,
        export: List<CommPayload>,
        sensedValues: AllSensorsPayload,
    ): BehaviourOutput<StateRepr, CommPayload, Unit, Unit> {
        println("Neighbours info: $export")
        val payload = CommPayload(context.id.show(), sensedValues.deviceSensor)
        return BehaviourOutput(state, payload, Unit, Unit)
    }
}
