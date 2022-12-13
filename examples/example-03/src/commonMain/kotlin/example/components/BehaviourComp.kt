package example.components

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.BehaviourOutput
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef
import it.nicolasfarabegoli.pulverization.runtime.dsl.NoVal
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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
) = coroutineScope {
    var neighboursComm = listOf<NeighboursMessage>()
    val jobComm = launch {
        comm.receiveFromComponent().collect {
            neighboursComm = neighboursComm.filter { e -> e.device != it.device } + it
        }
    }
    val jobSensor = launch {
        sensors.receiveFromComponent().collect {
            state.sendToComponent(Query("query selector")) // This is an example of query: do nothing.
            when (val lastState = state.receiveFromComponent().first()) {
                is Distances -> {
                    println("${behaviour.context.deviceID}: ${lastState.distances}")
                    val (newState, newComm, _, _) = behaviour(lastState, neighboursComm, it)
                    state.sendToComponent(newState)
                    comm.sendToComponent(newComm)
                }

                is Query -> error("The State should not respond with a query")
            }
        }
    }
    jobComm.join()
    jobSensor.join()
}
