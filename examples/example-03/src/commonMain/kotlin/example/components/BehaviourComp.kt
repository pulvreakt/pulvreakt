package example.components

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.BehaviourOutput
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef
import it.nicolasfarabegoli.pulverization.runtime.dsl.NoVal
import kotlinx.coroutines.delay
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

class BehaviourComp : Behaviour<StateOps, NeighboursMessage, DeviceSensors, NoVal, Unit> {
    override val context: Context by inject()

    override fun invoke(
        state: StateOps,
        export: List<NeighboursMessage>,
        sensedValues: DeviceSensors,
    ): BehaviourOutput<StateOps, NeighboursMessage, NoVal, Unit> {
        TODO("Not yet implemented")
    }
}

@Suppress("UNUSED_PARAMETER")
suspend fun behaviourLogics(
    behaviour: Behaviour<StateOps, NeighboursMessage, DeviceSensors, NoVal, Unit>,
    state: StateRef<StateOps>,
    comm: CommunicationRef<NeighboursMessage>,
    sensors: SensorsRef<DeviceSensors>,
    actuators: ActuatorsRef<NoVal>,
) {
    repeat(10) {
        delay(2.seconds)
    }
}
