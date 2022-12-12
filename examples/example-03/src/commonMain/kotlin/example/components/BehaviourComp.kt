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
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.seconds

class BehaviourComp : Behaviour<StateOps, NeighboursMessage, DeviceSensors, NoVal, Unit> {
    override val context: Context by inject()

    companion object {
        private const val R = 6371e3
        private const val ANGLE = 180.0
    }

    override fun invoke(
        state: StateOps,
        export: List<NeighboursMessage>,
        sensedValues: DeviceSensors,
    ): BehaviourOutput<StateOps, NeighboursMessage, NoVal, Unit> {
        val (myLat, myLong) = sensedValues.gps
        val distances = export.map { (device, location) ->
            val phi1 = myLat * PI / ANGLE
            val phi2 = location.lat * PI / ANGLE
            val deltaLat = (phi2 - phi1) * PI / ANGLE
            val deltaLong = (location.long - myLong) * PI / ANGLE

            val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                cos(phi1) * cos(phi2) *
                sin(deltaLong / 2) * sin(deltaLong / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            device to R * c
        }
        return BehaviourOutput(Distances(distances), NeighboursMessage(context.deviceID, sensedValues.gps), NoVal, Unit)
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
