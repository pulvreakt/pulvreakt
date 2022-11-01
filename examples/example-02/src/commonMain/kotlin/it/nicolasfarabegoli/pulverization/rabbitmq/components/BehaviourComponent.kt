package it.nicolasfarabegoli.pulverization.rabbitmq.components

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.SimpleRabbitmqBidirectionalCommunication
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.AllSensorsPayload
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.CommPayload
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceBehaviour
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class BehaviourComponent : DeviceComponent<RabbitmqContext> {
    override val context: RabbitmqContext by inject()
    private val behaviour: DeviceBehaviour by inject()
    private val state: DeviceState by inject()

    private val sensorsCommunicator =
        SimpleRabbitmqBidirectionalCommunication<Unit, AllSensorsPayload>(
            "sensors.exchange",
            "sensors/${context.id.show()}",
        )
    private val communication =
        SimpleRabbitmqBidirectionalCommunication<CommPayload, CommPayload>(
            "communication.exchange",
            "communication/${context.id.show()}",
        )

    private var lastNeighboursComm = emptyList<CommPayload>()
    private lateinit var deferred: Job

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun initialize() {
        sensorsCommunicator.initialize()
        communication.initialize()
        deferred = GlobalScope.launch {
            communication.receiveFromComponent().collect {
                lastNeighboursComm = lastNeighboursComm.filter { c -> c.deviceID != it.deviceID } + it
                lastNeighboursComm
            }
        }
    }

    override suspend fun finalize() {
        deferred.cancelAndJoin()
    }

    override suspend fun cycle() {
        sensorsCommunicator.receiveFromComponent().collect { sensedValues ->
            println("Device ${context.id.show()} received from neighbours: $lastNeighboursComm")
            val (newState, newComm, _, _) = behaviour(state.get(), lastNeighboursComm, sensedValues)
            state.update(newState)
            communication.sendToComponent(newComm)
        }
    }
}
