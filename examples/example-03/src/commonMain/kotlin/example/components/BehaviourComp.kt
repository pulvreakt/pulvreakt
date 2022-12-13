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
        private const val R = 6371
        private const val ANGLE = 180.0
    }

    override fun invoke(
        state: StateOps,
        export: List<NeighboursMessage>,
        sensedValues: DeviceSensors,
    ): BehaviourOutput<StateOps, NeighboursMessage, NoVal, Unit> {
        val (myLat, myLong) = sensedValues.gps
        val distances = export.map { (device, location) ->
            val dLat = (location.lat - myLat) * PI / ANGLE
            val dLon = (location.long - myLong) * PI / ANGLE
            val myLatRand = myLat * PI / ANGLE
            val otherLatRand = location.lat * PI / ANGLE

            val a = sin(dLat / 2) * sin(dLat / 2) + sin(dLon / 2) * sin(dLon / 2) * cos(myLatRand) * cos(otherLatRand)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            device to R * c
        }
        val min = distances.minByOrNull { it.second }
        return BehaviourOutput(
            Distances(distances, min),
            NeighboursMessage(context.deviceID, sensedValues.gps),
            NoVal,
            Unit,
        )
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
                    lastState.nearest?.let {
                        println("Device '${lastState.nearest.first}' is the nearest (${lastState.nearest.second})")
                    } ?: println("No information about the nearest")
                    val (newState, newComm, _, _) =
                        behaviour(lastState, neighboursComm.filter { e -> e.device != behaviour.context.deviceID }, it)
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
