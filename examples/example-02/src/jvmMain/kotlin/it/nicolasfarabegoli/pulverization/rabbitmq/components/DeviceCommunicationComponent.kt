package it.nicolasfarabegoli.pulverization.rabbitmq.components

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.SimpleRabbitmqBidirectionalCommunication
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.CommPayload
import it.nicolasfarabegoli.pulverization.rabbitmq.pure.DeviceCommunication
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.inject

class CommunicationComponent : DeviceComponent<RabbitmqContext> {
    override val context: RabbitmqContext by inject()

    private val deviceCommunication: DeviceCommunication by inject()

    private val componentCommunicator =
        SimpleRabbitmqBidirectionalCommunication<CommPayload, CommPayload>(
            CommunicationComponent to BehaviourComponent,
        )

    private val deferredReferences: MutableSet<Deferred<Unit>> = mutableSetOf()

    override suspend fun initialize(): Unit = coroutineScope {
        deviceCommunication.initialize()
        componentCommunicator.initialize()
        val deferComp = async {
            componentCommunicator.receiveFromComponent().collect {
                deviceCommunication.send(it)
            }
        }
        val deferComm = async {
            deviceCommunication.receive().collect { componentCommunicator.sendToComponent(it) }
        }
        deferredReferences += setOf(deferComp, deferComm)
    }

    override suspend fun finalize() = deferredReferences.forEach { it.cancelAndJoin() }

    override suspend fun cycle() {}
}
