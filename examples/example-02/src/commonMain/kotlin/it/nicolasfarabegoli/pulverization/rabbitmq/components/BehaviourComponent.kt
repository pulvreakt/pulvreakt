package it.nicolasfarabegoli.pulverization.rabbitmq.components

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.SimpleRabbitmqBidirectionalCommunication
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.SimpleRabbitmqReceiverCommunicator
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.AllSensorsPayload
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.CommPayload
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceBehaviour
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject

class BehaviourComponent : DeviceComponent<RabbitmqContext> {
    override val context: RabbitmqContext by inject()
    private val behaviour: DeviceBehaviour by inject()
    private val state: DeviceState by inject()

    private val sensorsCommunicator =
        SimpleRabbitmqReceiverCommunicator<AllSensorsPayload>("sensors.exchange", "sensors/${context.id.show()}")
    private val communication =
        SimpleRabbitmqBidirectionalCommunication<CommPayload, CommPayload>(
            "communication.exchange",
            "communication/${context.id.show()}",
        )

    private var lastNeighboursComm = emptyList<CommPayload>()

    override suspend fun initialize() {
        sensorsCommunicator.initialize()
        communication.initialize()
    }

    override suspend fun cycle() {
        val commFlow = communication.receiveFromComponent().map {
            lastNeighboursComm = lastNeighboursComm.filter { c -> c.deviceID == it.deviceID } + it
            lastNeighboursComm
        }

        sensorsCommunicator.receiveFromComponent()
            .combine(commFlow) { sensedValues, comm -> comm to sensedValues }
            .collect { (comm, sensedValues) ->
                println("Device ${context.id.show()} received from neighbours: $comm")
                val (newState, newComm, _, _) = behaviour(state.get(), comm, sensedValues)
                state.update(newState)
                communication.sendToComponent(newComm)
            }
    }
}
